package it.unicam.cs.mpgc.rpg130579.controller;

/**
 * Facciata principale del livello di controllo del gioco.
 * <p>
 * Aggrega le interfacce specializzate per esplorazione, dialoghi, avanzamento,
 * persistenza e statistiche del personaggio, disaccoppiando completamente il modello
 * dall'interfaccia utente tramite DTO immutabili ({@code view.*}).
 */
public interface GameController
        extends ExplorationController, DialogueController, ProgressController, PersistenceController,
        CharacterController { }