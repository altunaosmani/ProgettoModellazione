package it.unicam.cs.mpgc.rpg130579.persistence;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.ItemCatalog;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.LevelManager;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import java.util.*;

/**
 * Servizio che converte uno stato di gioco attivo ({@link GameState}) in {@link SaveData}
 * e viceversa tramite {@link SaveRestorer}.
 */
public class SaveService implements GamePersistence {
    private final SaveRepository repository;
    private final List<SaveContributor> contributors;
    private final SaveRestorer restorer;

    public SaveService(SaveRepository repository) {
        this(repository, List.of());
    }

    public SaveService(SaveRepository repository, List<SaveContributor> contributors) {
        if (repository == null) throw new IllegalArgumentException("Il repository non può essere nullo");
        if (contributors == null) throw new IllegalArgumentException("I contributori non possono essere nulli");
        Set<String> keys = new HashSet<>();
        for (SaveContributor contributor : contributors) {
            if (!keys.add(contributor.key())) {
                throw new IllegalArgumentException("Contributore duplicato: " + contributor.key());
            }
        }
        this.repository = repository;
        this.contributors = List.copyOf(contributors);
        this.restorer = new SaveRestorer(this.contributors);
    }

    @Override
    public boolean hasSave() {
        return repository.exists();
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
        restorer.restore(state, data.get(), catalog);
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
}