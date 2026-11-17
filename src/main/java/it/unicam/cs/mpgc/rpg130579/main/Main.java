package it.unicam.cs.mpgc.rpg130579.main;

import it.unicam.cs.mpgc.rpg130579.content.GameFactory;
import it.unicam.cs.mpgc.rpg130579.controller.DefaultGameController;
import it.unicam.cs.mpgc.rpg130579.controller.GameController;
import it.unicam.cs.mpgc.rpg130579.persistence.JsonSaveRepository;
import it.unicam.cs.mpgc.rpg130579.ui.ClasspathImageProvider;
import it.unicam.cs.mpgc.rpg130579.ui.GameApp;

/**
 * Punto di ingresso e composition root: qui si scelgono le implementazioni concrete
 * (contenuto, persistenza JSON, immagini da classpath) e si passano alla UI.
 * Un altro frontend (web, mobile) riuserebbe lo stesso GameController con un'altra interfaccia.
 * <p>
 * Esiste separato da GameApp perché una classe di avvio che estende Application
 * non parte da jar/classpath ("JavaFX runtime components are missing").
 */
public class Main {
    public static void main(String[] args) {
        GameController controller = new DefaultGameController(new GameFactory()::newGame, new JsonSaveRepository());
        GameApp.run(controller, new ClasspathImageProvider(), args);
    }
}