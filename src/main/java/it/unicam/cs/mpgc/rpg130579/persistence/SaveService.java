package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;

import java.io.UncheckedIOException;
import java.util.*;

/**
 * Servizio che converte uno stato di gioco attivo ({@link GameState}) in {@link SaveData}
 * e viceversa, validando la coerenza di tutti gli ID prima di applicare il ripristino.
 */
public class SaveService implements GamePersistence {
    private final SaveRepository repository;
    private final List<SaveContributor> contributors;

    public SaveService(SaveRepository repository) {
        if (repository == null) throw new IllegalArgumentException("Il repository non può essere nullo");
        this(repository, List.of());
    }

    public SaveService(SaveRepository repository, List<SaveContributor> contributors) {
        if (repository == null) throw new IllegalArgumentException("Il repository non può essere nullo");
        if (contributors == null) throw new IllegalArgumentException("I contributori non possono essere nulli");
        Set<String> keys = new HashSet<>();
        for (SaveContributor contributor : contributors) {
            if (!keys.add(contributor.key()))
                throw new IllegalArgumentException("Contributore duplicato: " + contributor.key());
        }
        this.repository = repository;
        this.contributors = List.copyOf(contributors);
    }

    private static Mechanism findMechanism(LevelManager levels, String id) {
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            Optional<Mechanism> mechanism = levels.getLevelAt(i).findMechanismById(id);
            if (mechanism.isPresent()) return mechanism.get();
        }
        throw new IllegalStateException("Meccanismo salvato inesistente: " + id);
    }

    private static void removeFromAllLocations(LevelManager levels, String itemId) {
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            for (Location place : levels.getLevelAt(i).getPlaces()) {
                place.removeItem(itemId);
            }
        }
    }

    @Override
    public boolean hasSave() {
        try {
            return repository.load().isPresent();
        } catch (UncheckedIOException e) {
            return false;
        }
    }

    @Override
    public void save(GameState state) {
        repository.save(capture(state));
    }

    @Override
    public boolean load(GameState state, ItemCatalog catalog) {
        if (catalog == null) throw new IllegalArgumentException("Il catalogo oggetti non può essere nullo");
        Optional<SaveData> data = repository.load();
        if (data.isEmpty()) return false;
        restore(state, data.get(), catalog);
        return true;
    }

    SaveData capture(GameState state) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        LevelManager levels = state.getLevelManager();
        Player player = state.getPlayer();
        StoryProgress progress = state.getProgress();

        List<String> completedLevels = new ArrayList<>();
        List<String> unlockedMechanisms = new ArrayList<>();
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            Level level = levels.getLevelAt(i);
            if (level.isCompleted()) completedLevels.add(level.getId());
            for (Mechanism mechanism : level.getMechanisms()) {
                if (mechanism.isUnlocked()) unlockedMechanisms.add(mechanism.getId());
            }
        }
        List<String> inventory = player.getInventory().stream().map(Item::getId).toList();

        Map<String, Integer> attributes = new LinkedHashMap<>();
        for (Attribute attribute : Attribute.values()) {
            attributes.put(attribute.name(), player.getStats().get(attribute));
        }

        Map<String, Map<String, String>> extensions = new LinkedHashMap<>();
        for (SaveContributor contributor : contributors) {
            extensions.put(contributor.key(), contributor.capture(state));
        }

        return new SaveData(
                state.getCurrentLevel().getId(),
                player.getCurrentLocation().getId(),
                completedLevels,
                inventory,
                new ArrayList<>(progress.getSolvedPuzzleIds()),
                unlockedMechanisms,
                progress.getFlags(),
                state.isGameEnd(),
                state.isWin(),
                attributes,
                extensions);
    }

    void restore(GameState state, SaveData data, ItemCatalog catalog) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        if (data.getVersion() < SaveData.MIN_SUPPORTED_VERSION || data.getVersion() > SaveData.CURRENT_VERSION)
            throw new IllegalStateException("Versione di salvataggio non supportata: " + data.getVersion());

        LevelManager levels = state.getLevelManager();

        Level currentLevel = levels.findLevelById(data.getCurrentLevelId())
                .orElseThrow(() -> new IllegalStateException("Livello salvato inesistente: " + data.getCurrentLevelId()));

        Location location = currentLevel.findPlaceById(data.getCurrentLocationId())
                .orElseThrow(() -> new IllegalStateException("Luogo salvato inesistente: " + data.getCurrentLocationId()));

        List<Level> completedLevels = new ArrayList<>();
        for (String id : data.getCompletedLevelIds()) {
            completedLevels.add(levels.findLevelById(id)
                    .orElseThrow(() -> new IllegalStateException("Livello completato inesistente: " + id)));
        }

        List<Mechanism> unlocked = new ArrayList<>();
        for (String id : data.getUnlockedMechanismIds()) {
            unlocked.add(findMechanism(levels, id));
        }

        List<Item> inventory = new ArrayList<>();
        for (String id : data.getInventoryItemIds()) {
            inventory.add(catalog.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Oggetto salvato inesistente: " + id)));
        }

        Map<Attribute, Integer> attributes = new EnumMap<>(Attribute.class);
        for (Map.Entry<String, Integer> entry : data.getAttributes().entrySet()) {
            Integer value = entry.getValue();
            if (value == null || value < 0)
                throw new IllegalStateException("Valore di attributo non valido: " + entry.getKey());
            try {
                attributes.put(Attribute.valueOf(entry.getKey()), value);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Attributo salvato inesistente: " + entry.getKey());
            }
        }

        // fase 1: le estensioni validano i propri dati senza modificare lo stato
        List<Runnable> extensionRestores = new ArrayList<>();
        for (SaveContributor contributor : contributors) {
            Map<String, String> saved = data.getExtensions().get(contributor.key());
            if (saved != null) extensionRestores.add(contributor.prepareRestore(state, catalog, saved));
        }

        // fase 2: da qui in poi si applica tutto
        levels.setCurrentLevelById(currentLevel.getId());
        for (Level level : completedLevels) {
            level.getQuest().complete();
        }
        unlocked.forEach(Mechanism::restoreUnlocked);

        Player player = state.getPlayer();
        for (Item item : new ArrayList<>(player.getInventory())) player.removeItem(item);
        for (Item item : inventory) {
            player.addItem(item);
            removeFromAllLocations(levels, item.getId());
        }
        player.setCurrentLocation(location);
        player.getStats().restore(attributes);

        state.getProgress().restore(data.getSolvedPuzzleIds(), data.getFlags());
        if (data.isGameEnded()) state.restoreEnded(data.isWon());
        extensionRestores.forEach(Runnable::run);
    }
}