package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueContext;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueSession;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;

/**
 * Motore delle regole di gioco che opera direttamente sul modello ({@link GameState}).
 * <p>
 * Gestisce la logica transazionale delle azioni del giocatore: spostamenti tra luoghi,
 * raccolta di oggetti, sblocco di meccanismi con assegnazione unica delle ricompense,
 * creazione di sessioni di dialogo e verifica del completamento delle missioni.
 */
public class GameManager {
    private final GameState state;

    /**
     * Crea un manager associato allo stato di gioco fornito.
     *
     * @param state stato della partita da gestire
     * @throws IllegalArgumentException se {@code state} è {@code null}
     */
    public GameManager(GameState state) {
        if (state == null) throw new IllegalArgumentException("Lo stato di gioco non può essere nullo");
        this.state = state;
    }

    public GameState getCurrentState() {
        return state;
    }

    public Level getCurrentLevel() {
        return state.getCurrentLevel();
    }

    public boolean isGameOver() {
        return state.isGameEnd();
    }

    public boolean hasWon() {
        return state.isWin();
    }

    public Location getLocation(String locationId) {
        if (locationId == null || locationId.isBlank())
            throw new IllegalArgumentException("L'ID del luogo non può essere vuoto");
        Location location = getCurrentLevel().getPlaceById(locationId);
        if (location == null) throw new IllegalArgumentException("Luogo non trovato: " + locationId);
        return location;
    }

    /**
     * Avvia un'interazione con un NPC.
     *
     * @param npcId identificativo del personaggio non giocante
     * @return nuova {@link DialogueSession} pronta per essere percorsa
     * @throws IllegalStateException    se la partita è già terminata
     * @throws IllegalArgumentException se personaggio non esiste
     */
    public DialogueSession interactWithNPC(String npcId) {
        ensureRunning();
        if (npcId == null || npcId.isBlank())
            throw new IllegalArgumentException("L'ID del personaggio non può essere vuoto");
        Player player = state.getPlayer();
        NPC npc = player.getCurrentLocation().getNPCById(npcId);
        if (npc == null) throw new IllegalArgumentException("Personaggio non trovato: " + npcId);
        DialogueContext context = new DialogueContext(player, state.getProgress());
        return new DialogueSession(npc.interact(player, state.getProgress()), context);
    }

    public void advanceToNextLevel() {
        ensureRunning();
        state.nextLevel();
    }

    /**
     * Tenta di spostare il giocatore lungo un'uscita del luogo corrente.
     *
     * @param exitId identificativo dell'uscita
     * @return {@code true} se lo spostamento è avvenuto, {@code false} se l'uscita è bloccata
     * @throws IllegalStateException    se la partita è già terminata
     * @throws IllegalArgumentException se l'uscita non esiste nel luogo corrente
     */
    public boolean move(String exitId) {
        ensureRunning();
        Player player = state.getPlayer();
        Exit exit = player.getCurrentLocation().getExitById(exitId);
        if (exit == null) throw new IllegalArgumentException("Uscita non trovata: " + exitId);
        if (!exit.isPassable()) return false;
        player.setCurrentLocation(getLocation(exit.getDestinationLocationId()));
        return true;
    }

    public boolean takeItem(String itemId) {
        ensureRunning();
        Player player = state.getPlayer();
        Location location = player.getCurrentLocation();
        Item item = location.getItemById(itemId);
        if (item == null) throw new IllegalArgumentException("Oggetto non trovato: " + itemId);
        if (!player.addItem(item)) return false;
        location.removeItem(itemId);
        return true;
    }

    /**
     * Tenta di sbloccare un meccanismo nel luogo corrente, registrando l'eventuale enigma
     * come risolto e assegnando la ricompensa in attributi alla prima apertura.
     *
     * @param mechanismId identificativo del meccanismo
     * @param answer      risposta fornita dal giocatore
     * @return {@code true} se il meccanismo risulta sbloccato al termine dell'operazione
     * @throws IllegalStateException    se la partita è già terminata
     * @throws IllegalArgumentException se il meccanismo non esiste nel luogo corrente
     */
    public boolean unlockMechanism(String mechanismId, PuzzleAnswer answer) {
        ensureRunning();
        Player player = state.getPlayer();
        Mechanism mechanism = player.getCurrentLocation().getMechanismById(mechanismId);
        if (mechanism == null) throw new IllegalArgumentException("Meccanismo non trovato: " + mechanismId);
        boolean wasLocked = mechanism.isLocked();
        boolean unlocked = mechanism.tryUnlock(player, answer);
        if (unlocked && wasLocked) {
            mechanism.getRequiredPuzzle().ifPresent(p -> state.getProgress().markPuzzleSolved(p.getId()));
            // Il bonus si assegna solo alla prima apertura.
            mechanism.getReward().ifPresent(r -> player.getStats().increase(r.attribute(), r.amount()));
        }
        return unlocked;
    }

    /**
     * Verifica se la missione del livello corrente soddisfa le sue condizioni e, in tal caso,
     * la contrassegna come completata.
     *
     * @return {@code true} se la missione è stata appena completata, {@code false} altrimenti
     */
    public boolean completeCurrentQuest() {
        if (state.isGameEnd()) return false;
        Quest quest = getCurrentLevel().getQuest();
        if (!quest.isCompletable(state.getPlayer(), state.getProgress())) return false;
        quest.complete();
        return true;
    }

    private void ensureRunning() {
        if (state.isGameEnd()) throw new IllegalStateException("La partita è già terminata");
    }
}