package it.unicam.cs.mpgc.rpg130579.Persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Fotografia dello stato di una partita. Contiene solo id e valori semplici,
 * mai riferimenti al modello, cosi' si serializza in JSON senza sorprese.
 */
public class SaveData {
    public static final int CURRENT_VERSION = 1;

    private int version = CURRENT_VERSION;
    private String currentLevelId;
    private String currentLocationId;
    private List<String> completedLevelIds = new ArrayList<>();
    private List<String> inventoryItemIds = new ArrayList<>();
    private List<String> solvedPuzzleIds = new ArrayList<>();
    private List<String> unlockedMechanismIds = new ArrayList<>();
    private Map<String, String> flags = new LinkedHashMap<>();
    private boolean gameEnded;
    private boolean won;

    /** Richiesto da Gson. */
    public SaveData() {
    }

    public SaveData(String currentLevelId,
                    String currentLocationId,
                    Collection<String> completedLevelIds,
                    Collection<String> inventoryItemIds,
                    Collection<String> solvedPuzzleIds,
                    Collection<String> unlockedMechanismIds,
                    Map<String, String> flags,
                    boolean gameEnded,
                    boolean won) {
        if (currentLevelId == null || currentLevelId.isBlank()) throw new IllegalArgumentException("L'ID del livello non può essere vuoto");
        if (currentLocationId == null || currentLocationId.isBlank()) throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        this.currentLevelId = currentLevelId;
        this.currentLocationId = currentLocationId;
        this.completedLevelIds = copy(completedLevelIds);
        this.inventoryItemIds = copy(inventoryItemIds);
        this.solvedPuzzleIds = copy(solvedPuzzleIds);
        this.unlockedMechanismIds = copy(unlockedMechanismIds);
        this.flags = flags == null ? new LinkedHashMap<>() : new LinkedHashMap<>(flags);
        this.gameEnded = gameEnded;
        this.won = won;
    }

    public int getVersion() {
        return version;
    }

    public String getCurrentLevelId() {
        return currentLevelId;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public List<String> getCompletedLevelIds() {
        return view(completedLevelIds);
    }

    public List<String> getInventoryItemIds() {
        return view(inventoryItemIds);
    }

    public List<String> getSolvedPuzzleIds() {
        return view(solvedPuzzleIds);
    }

    public List<String> getUnlockedMechanismIds() {
        return view(unlockedMechanismIds);
    }

    public Map<String, String> getFlags() {
        return flags == null ? Map.of() : Collections.unmodifiableMap(flags);
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public boolean isWon() {
        return won;
    }

    private static List<String> copy(Collection<String> source) {
        return source == null ? new ArrayList<>() : new ArrayList<>(source);
    }

    // Gson lascia null i campi assenti nel file: le viste li trattano come vuoti.
    private static List<String> view(List<String> list) {
        return list == null ? List.of() : Collections.unmodifiableList(list);
    }
}