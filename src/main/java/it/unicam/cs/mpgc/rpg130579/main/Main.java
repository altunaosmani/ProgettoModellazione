package it.unicam.cs.mpgc.rpg130579.main;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.controller.DefaultGameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.ui.ClasspathImageProvider;
import it.unicam.cs.mpgc.rpg130579.ui.GameApp;

/**
 * Punto di ingresso dell'applicazione e <i>composition root</i> del progetto.
 * <p>
 * Si occupa di istanziare e collegare le implementazioni concrete del dominio,
 * della persistenza (JSON) e dei provider grafici, delegando poi l'avvio
 * dell'interfaccia grafica JavaFX a {@link GameApp}.
 * <p>
 * È mantenuta separata da {@link GameApp} per consentire l'avvio corretto
 * da archivi JAR senza errori legati ai componenti runtime di JavaFX.
 */
public class Main {
    /**
     * Metodo principale che avvia il gioco.
     *
     * @param args argomenti da riga di comando inoltrati all'applicazione JavaFX
     */
    public static void main(String[] args) {
        GameController controller = new DefaultGameController(new GameFactory()::newGame, new JsonSaveRepository());
        GameApp.run(controller, new ClasspathImageProvider(), args);
    }
}