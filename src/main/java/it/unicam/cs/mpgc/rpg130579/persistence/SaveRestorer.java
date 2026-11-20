package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import java.util.*;

/**
 * Valida la coerenza di un {@link SaveData} rispetto al mondo di gioco e ne applica
 * il ripristino in modo atomico sullo stato attivo.
 */
final class SaveRestorer {
    private final List<SaveContributor> contributors;

    SaveRestorer(List<SaveContributor> contributors) {
        this.contributors = List.copyOf(contributors);
    }

    void restore(GameState state, SaveData data, ItemCatalog catalog) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        validateVersion(data);

        LevelManager levels = state.getLevelManager();
        Level currentLevel = resolveCurrentLevel(levels, data);
        Location location = resolveLocation(currentLevel, data);
        List<Level> completedLevels = resolveCompletedLevels(levels, data);
        List<Mechanism> unlockedMechanisms = resolveUnlockedMechanisms(levels, data);
        List<Item> inventory = resolveInventory(catalog, data);
        Map<Attribute, Integer> attributes = resolveAttributes(data);

        List<Runnable> extensionRestores = new ArrayList<>();
        for (SaveContributor contributor : contributors) {
            Map<String, String> saved = data.getExtensions().get(contributor.key());
            if (saved != null) {
                extensionRestores.add(contributor.prepareRestore(state, catalog, saved));
            }
        }

        apply(state, data, levels, currentLevel, location, completedLevels,
                unlockedMechanisms, inventory, attributes, extensionRestores);
    }

    private void validateVersion(SaveData data) {
        if (data.getVersion() < SaveData.MIN_SUPPORTED_VERSION || data.getVersion() > SaveData.CURRENT_VERSION) {
            throw new IllegalStateException("Versione di salvataggio non supportata: " + data.getVersion());
        }
    }

    private Level resolveCurrentLevel(LevelManager levels, SaveData data) {
        return levels.findLevelById(data.getCurrentLevelId())
                .orElseThrow(() -> new IllegalStateException("Livello salvato inesistente: " + data.getCurrentLevelId()));
    }

    private Location resolveLocation(Level level, SaveData data) {
        return level.findPlaceById(data.getCurrentLocationId())
                .orElseThrow(() -> new IllegalStateException("Luogo salvato inesistente: " + data.getCurrentLocationId()));
    }

    private List<Level> resolveCompletedLevels(LevelManager levels, SaveData data) {
        List<Level> result = new ArrayList<>();
        for (String id : data.getCompletedLevelIds()) {
            result.add(levels.findLevelById(id)
                    .orElseThrow(() -> new IllegalStateException("Livello completato inesistente: " + id)));
        }
        return result;
    }

    private List<Mechanism> resolveUnlockedMechanisms(LevelManager levels, SaveData data) {
        List<Mechanism> result = new ArrayList<>();
        for (String id : data.getUnlockedMechanismIds()) {
            result.add(findMechanism(levels, id));
        }
        return result;
    }

    private List<Item> resolveInventory(ItemCatalog catalog, SaveData data) {
        List<Item> result = new ArrayList<>();
        for (String id : data.getInventoryItemIds()) {
            result.add(catalog.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Oggetto salvato inesistente: " + id)));
        }
        return result;
    }

    private Map<Attribute, Integer> resolveAttributes(SaveData data) {
        Map<Attribute, Integer> result = new EnumMap<>(Attribute.class);
        for (Map.Entry<String, Integer> entry : data.getAttributes().entrySet()) {
            Integer value = entry.getValue();
            if (value == null || value < 0) {
                throw new IllegalStateException("Valore di attributo non valido: " + entry.getKey());
            }
            try {
                result.put(Attribute.valueOf(entry.getKey()), value);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("Attributo salvato inesistente: " + entry.getKey());
            }
        }
        return result;
    }

    private void apply(GameState state, SaveData data, LevelManager levels, Level currentLevel,
                       Location location, List<Level> completedLevels, List<Mechanism> unlocked,
                       List<Item> inventory, Map<Attribute, Integer> attributes,
                       List<Runnable> extensionRestores) {
        levels.setCurrentLevelById(currentLevel.getId());
        for (Level level : completedLevels) {
            level.getQuest().complete();
        }
        unlocked.forEach(Mechanism::restoreUnlocked);

        Player player = state.getPlayer();
        for (Item item : new ArrayList<>(player.getInventory())) {
            player.removeItem(item);
        }
        for (Item item : inventory) {
            player.addItem(item);
            removeFromAllLocations(levels, item.getId());
        }
        player.setCurrentLocation(location);
        player.getStats().restore(attributes);
        state.getProgress().restore(data.getSolvedPuzzleIds(), data.getFlags());
        if (data.isGameEnded()) {
            state.restoreEnded(data.isWon());
        }
        extensionRestores.forEach(Runnable::run);
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
}