package it.unicam.cs.mpgc.rpg130579.model.state;

import java.util.*;

/**
 * Traccia gli eventi permanenti avvenuti durante la partita: gli ID degli enigmi risolti
 * e una mappa di flag narrativi chiave-valore (normalizzati in minuscolo).
 */
public class StoryProgress {
    private final Set<String> solvedPuzzleIds = new HashSet<>();
    private final Map<String, String> flags = new HashMap<>();

    /**
     * Verifica se un enigma risulta già risolto.
     *
     * @param puzzleId identificativo dell'enigma
     * @return {@code true} se l'enigma è stato risolto
     */
    public boolean isPuzzleSolved(String puzzleId) {
        return solvedPuzzleIds.contains(normalize(puzzleId, "L'ID dell'enigma"));
    }

    /**
     * @return insieme non modificabile degli ID degli enigmi risolti
     */
    public Set<String> getSolvedPuzzleIds() {
        return Collections.unmodifiableSet(solvedPuzzleIds);
    }

    /**
     * Imposta o aggiorna un flag narrativo normalizzando la chiave in minuscolo.
     *
     * @param key chiave del flag
     * @param value valore testuale da associare
     * @throws IllegalArgumentException se chiave o valore sono nulli o vuoti
     */
    public void setFlag(String key, String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Il valore del flag non può essere vuoto");
        flags.put(normalize(key, "La chiave del flag"), value.trim());
    }

    /**
     * Recupera il valore associato a un flag narrativo.
     *
     * @param key chiave del flag
     * @return {@link Optional} con il valore se presente, altrimenti vuoto
     */
    public Optional<String> getFlag(String key) {
        return Optional.ofNullable(flags.get(normalize(key, "La chiave del flag")));
    }

    /**
     * Verifica se un flag narrativo è impostato sul valore atteso (case-insensitive).
     *
     * @param key chiave del flag
     * @param value valore atteso
     * @return {@code true} se il flag esiste e corrisponde al valore indicato
     */
    public boolean hasFlag(String key, String value) {
        return value != null && getFlag(key).map(v -> v.equalsIgnoreCase(value.trim())).orElse(false);
    }

    /**
     * @return mappa non modificabile di tutti i flag narrativi impostati
     */
    public Map<String, String> getFlags() {
        return Collections.unmodifiableMap(flags);
    }

    private String normalize(String text, String label) {
        if (text == null || text.isBlank()) throw new IllegalArgumentException(label + " non può essere vuoto");
        return text.trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Registra un enigma come risolto.
     *
     * @param puzzleId identificativo dell'enigma risolto
     */
    public void markPuzzleSolved(String puzzleId) {
        solvedPuzzleIds.add(normalize(puzzleId, "L'ID dell'enigma"));
    }

    /**
     * Ripristina completamente gli enigmi risolti e i flag a partire dai dati di un salvataggio.
     *
     * @param solvedIds collezione degli ID degli enigmi risolti
     * @param savedFlags mappa dei flag narrativi salvati
     */
    public void restore(Collection<String> solvedIds, Map<String, String> savedFlags) {
        solvedPuzzleIds.clear();
        flags.clear();
        solvedIds.forEach(this::markPuzzleSolved);
        savedFlags.forEach(this::setFlag);
    }
}
