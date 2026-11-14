package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;

import java.util.List;

public final class StoryIntros {

    public static IntroScreen prologue() {
        return new IntroScreen("PROLOGO", List.of(
                "Tanusha, un piccolo villaggio ai margini di una grande foresta.",
                "Eren è un giovane elfo cresciuto senza sua madre. Elara è scomparsa quando lui era molto piccolo: "
                        + "in paese raccontano che una notte se ne sia semplicemente andata, senza fare più ritorno.",
                "Eren non ha mai creduto davvero a quella versione. Di lei gli resta solo un medaglione d'argento, "
                        + "decorato con un occhio circondato da tre rami.",
                "Una mattina, dopo anni di silenzio, il medaglione si apre da solo e rivela una frase: "
                        + "\"Quando la foresta parlerà, ascolta.\"",
                "Da questo momento Eren ha un solo obiettivo: seguire le tracce di sua madre "
                        + "e scoprire che cosa le sia davvero accaduto."));
    }

    private StoryIntros() { }
}
