package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.view.*;

import java.util.List;

/**
 * Contratto per la conversione delle entità del modello nei DTO immutabili destinati alla UI.
 * Consente di sostituire la rappresentazione della vista senza alterare la logica di gioco.
 */
public interface ViewMapper {
    /**
     * Converte un luogo e i suoi elementi visibili nella corrispondente vista, filtrando
     * dettagli e indizi in base allo stato del giocatore.
     *
     * @param location luogo da convertire
     * @param player protagonista corrente
     * @param progress progressi narrativi della partita
     * @return DTO rappresentante il luogo
     */
    LocationView toView(Location location, Player player, StoryProgress progress);

    /**
     * Converte una lista di oggetti di dominio nelle rispettive viste.
     *
     * @param items lista di oggetti da convertire
     * @return lista immodificabile di {@link ItemView}
     */
    List<ItemView> toItemViews(List<Item> items);

    /**
     * Converte le statistiche del protagonista in una vista comprensiva di valori effettivi e bonus.
     *
     * @param player protagonista di cui estrarre gli attributi
     * @return DTO delle statistiche
     */
    StatsView toView(Player player);

    /**
     * Converte la schermata introduttiva di dominio nel relativo DTO di presentazione.
     *
     * @param intro dati dell'introduzione
     * @return DTO della schermata iniziale
     */
    IntroView toView(IntroScreen intro);

    /**
     * Converte le informazioni del livello corrente e della sua missione in un DTO.
     *
     * @param level livello attivo
     * @param number numero ordinale del livello (a base 1)
     * @param total numero totale di livelli nel gioco
     * @return DTO informativo del livello
     */
    LevelInfoView toView(Level level, int number, int total);

    /**
     * Converte la schermata finale selezionata nel relativo DTO di presentazione.
     *
     * @param ending schermata finale di dominio
     * @return DTO del finale
     */
    EndingView toView(EndingScreen ending);
}