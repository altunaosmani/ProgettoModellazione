package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.*;

import java.util.Optional;

/**
 * Interrogazione e avanzamento della progressione tra i livelli e i finali.
 */
public interface ProgressController {
    /**
     * @return stato corrente di completamento del livello e della partita
     */
    GameStatus status();

    /**
     * Avanza al livello successivo se quello attuale è completato, oppure conclude la partita
     * con la vittoria se l'ultimo livello è stato terminato.
     *
     * @return esito dell'avanzamento
     */
    ActionResult advance();

    /**
     * @return vista del finale selezionato se la partita è stata vinta, altrimenti {@link Optional#empty()}
     */
    Optional<EndingView> ending();

    /**
     * @return dati della schermata introduttiva della partita
     */
    IntroView intro();

    /**
     * @return informazioni sul livello corrente e sulla relativa missione
     */
    LevelInfoView levelInfo();
}
