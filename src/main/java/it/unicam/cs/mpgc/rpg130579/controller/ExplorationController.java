package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;

import java.util.List;

/**
 * Operazioni di esplorazione del mondo e gestione dell'inventario.
 */
public interface ExplorationController {
    /** @return vista del luogo in cui si trova attualmente il giocatore */
    LocationView currentLocation();

    /** @return lista degli oggetti attualmente nell'inventario del giocatore */
    List<ItemView> inventory();

    /**
     * Tenta di spostare il giocatore attraverso l'uscita specificata.
     *
     * @param exitId identificativo dell'uscita del luogo corrente
     * @return esito dell'operazione con eventuale messaggio di errore se bloccata
     */
    ActionResult move(String exitId);

    /**
     * Raccoglie un oggetto presente nel luogo corrente e lo aggiunge all'inventario.
     *
     * @param itemId identificativo dell'oggetto da raccogliere
     * @return esito dell'operazione, comprensivo di eventuali bonus agli attributi ottenuti
     */
    ActionResult takeItem(String itemId);

    /**
     * Tenta di sbloccare un meccanismo presente nel luogo corrente.
     *
     * @param mechanismId identificativo del meccanismo
     * @param answer      lista ordinata di risposte per l'eventuale enigma associato (può essere vuota)
     * @return esito del tentativo di sblocco
     */
    ActionResult unlock(String mechanismId, List<String> answer);
}
