package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.DialogueView;

/**
 * Gestione delle conversazioni interattive con i personaggi non giocanti (NPC).
 */
public interface DialogueController {
    /**
     * Avvia una nuova sessione di dialogo con un NPC nel luogo corrente.
     *
     * @param npcId identificativo del personaggio con cui parlare
     * @return stato iniziale del dialogo (battute iniziali ed eventuali scelte)
     */
    DialogueView talkTo(String npcId);

    /**
     * Seleziona una delle opzioni di risposta disponibili nel dialogo attivo.
     *
     * @param index indice a base zero della scelta selezionata tra quelle visibili
     * @return stato aggiornato del dialogo dopo l'applicazione degli effetti della scelta
     */
    DialogueView choose(int index);

    /**
     * Chiude la conversazione corrente, anche se è stata interrotta a metà, e azzera la sessione.
     *
     * @return riepilogo di ciò che è cambiato durante il dialogo (attributi guadagnati,
     * missione completata); messaggio vuoto se non è cambiato nulla
     */
    ActionResult endDialogue();
}
