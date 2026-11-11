package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import it.unicam.cs.mpgc.rpg130579.persistence.SaveData;
import it.unicam.cs.mpgc.rpg130579.persistence.SaveRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Traduce lo stato di gioco in SaveData e viceversa.
 * Il ripristino va fatto su un GameState appena costruito dal contenuto di gioco
 * (livelli, luoghi e oggetti nel loro stato iniziale).
 */
public class SaveService {
    private final SaveRepository repository;
    private final ItemCatalog catalog;

    public SaveService(SaveRepository repository, ItemCatalog catalog) {
        if (repository == null) throw new IllegalArgumentException("Il repository non può essere nullo");
        if (catalog == null) throw new IllegalArgumentException("Il catalogo oggetti non può essere nullo");
        this.repository = repository;
        this.catalog = catalog;
    }

    public void save(GameState state) {
        repository.save(capture(state));
    }

    /**
     * @return false se non esiste nessun salvataggio
     * @throws IllegalStateException se il salvataggio non è coerente col contenuto di gioco;
     *                               in tal caso lo stato non viene modificato
     */
    public boolean load(GameState state) {
        Optional<SaveData> data = repository.load();
        if (data.isEmpty()) return false;
        restore(state, data.get());
        return true;
    }

    SaveData capture(GameState state) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        LevelManager levels = state.getGameLevel();
        Player player = state.getPlayer();
        StoryProgress progress = state.getProgress();

        List<String> completedLevels = new ArrayList<>();
        List<String> unlockedMechanisms = new ArrayList<>();
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            Level level = levels.getLevelAt(i);
            if (level.isCompleted()) completedLevels.add(level.getId());
            for (int m = 0; m < level.getSizeMechanism(); m++) {
                Mechanism mechanism = level.getMechanismAt(m);
                if (mechanism.isUnlocked()) unlockedMechanisms.add(mechanism.getId());
            }
        }
        List<String> inventory = player.getInventory().stream().map(Item::getId).toList();

        return new SaveData(
                state.getCurrentLevel().getId(),
                player.getCurrentLocation().getId(),
                completedLevels,
                inventory,
                new ArrayList<>(progress.getSolvedPuzzleIds()),
                unlockedMechanisms,
                progress.getFlags(),
                state.isGameEnd(),
                state.isWin());
    }

    void restore(GameState state, SaveData data) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        if (data.getVersion() != SaveData.CURRENT_VERSION)
            throw new IllegalStateException("Versione di salvataggio non supportata: " + data.getVersion());

        LevelManager levels = state.getGameLevel();

        // Fase 1: risolvo tutti gli id. Se qualcosa non torna, lancio prima di modificare lo stato.
        Level currentLevel = levels.getLevelById(data.getCurrentLevelId());
        if (currentLevel == null) throw new IllegalStateException("Livello salvato inesistente: " + data.getCurrentLevelId());

        Location location = currentLevel.getPlaceById(data.getCurrentLocationId());
        if (location == null) throw new IllegalStateException("Luogo salvato inesistente: " + data.getCurrentLocationId());

        List<Level> completedLevels = new ArrayList<>();
        for (String id : data.getCompletedLevelIds()) {
            Level level = levels.getLevelById(id);
            if (level == null) throw new IllegalStateException("Livello completato inesistente: " + id);
            completedLevels.add(level);
        }

        List<Mechanism> unlocked = new ArrayList<>();
        for (String id : data.getUnlockedMechanismIds()) {
            unlocked.add(findMechanism(levels, id));
        }

        List<Item> inventory = new ArrayList<>();
        for (String id : data.getInventoryItemIds()) {
            inventory.add(catalog.findById(id).orElseThrow(() -> new IllegalStateException("Oggetto salvato inesistente: " + id)));
        }

        // Fase 2: applico.
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

        state.getProgress().restore(data.getSolvedPuzzleIds(), data.getFlags());
        if (data.isGameEnded()) state.restoreEnded(data.isWon());
    }

    private static Mechanism findMechanism(LevelManager levels, String id) {
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            Mechanism mechanism = levels.getLevelAt(i).getMechanismById(id);
            if (mechanism != null) return mechanism;
        }
        throw new IllegalStateException("Meccanismo salvato inesistente: " + id);
    }

    // Un oggetto in inventario non deve comparire anche nel luogo dove era all'inizio.
    private static void removeFromAllLocations(LevelManager levels, String itemId) {
        for (int i = 0; i < levels.getLevelsSize(); i++) {
            Level level = levels.getLevelAt(i);
            for (int p = 0; p < level.getSizePlaces(); p++) {
                level.getPlaceAt(p).removeItem(itemId);
            }
        }
    }
}