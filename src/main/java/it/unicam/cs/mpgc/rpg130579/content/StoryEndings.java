package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;

public final class StoryEndings {

    public static EndingScreen victory() {
        return new EndingScreen("THE LOST ELF", "QUEST COMPLETATA", "HAI TROVATO ELARA",
                "Dopo anni di ricerca, Eren ha finalmente ritrovato sua madre e scoperto la verità sulla sua scomparsa.",
                "VITTORIA");
    }

    private StoryEndings() {
    }
}
