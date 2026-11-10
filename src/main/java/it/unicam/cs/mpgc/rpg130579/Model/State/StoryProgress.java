package it.unicam.cs.mpgc.rpg130579.Model.State;

import java.util.*;


public class StoryProgress {
    private final Set<String> solvedPuzzleIds = new HashSet<>();
    private final Map<String, String> flags = new HashMap<>();

    public boolean isPuzzleSolved(String puzzleId) {
        return solvedPuzzleIds.contains(normalize(puzzleId, "L'ID dell'enigma"));
    }

    public Set<String> getSolvedPuzzleIds() {
        return Collections.unmodifiableSet(solvedPuzzleIds);
    }

    public void setFlag(String key, String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("Il valore del flag non può essere vuoto");
        flags.put(normalize(key, "La chiave del flag"), value.trim());
    }

    public Optional<String> getFlag(String key) {
        return Optional.ofNullable(flags.get(normalize(key, "La chiave del flag")));
    }

    public boolean hasFlag(String key, String value) {
        return value != null && getFlag(key).map(v -> v.equalsIgnoreCase(value.trim())).orElse(false);
    }

    public Map<String, String> getFlags() {
        return Collections.unmodifiableMap(flags);
    }

    private String normalize(String text, String label) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException(label + " non può essere vuoto");
        return text.trim().toLowerCase(Locale.ROOT);
    }

    public void markPuzzleSolved(String puzzleId) {
        solvedPuzzleIds.add(normalize(puzzleId, "L'ID dell'enigma"));
    }

    public void restore(Collection<String> solvedIds, Map<String, String> savedFlags) {
        solvedPuzzleIds.clear();
        flags.clear();
        solvedIds.forEach(this::markPuzzleSolved);
        savedFlags.forEach(this::setFlag);
    }
}
