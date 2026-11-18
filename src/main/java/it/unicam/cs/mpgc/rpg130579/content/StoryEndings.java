package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AllOfCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AttributeCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.FlagCondition;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingCatalog;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;

import java.util.List;

/**
 * Definisce i possibili finali di vittoria del gioco e le condizioni (attributi o flag)
 * che determinano quale variante mostrare al termine dell'avventura.
 */
public final class StoryEndings {
    private static final String TITLE = "THE LOST ELF";
    private static final String HEADLINE = "QUEST COMPLETATA";
    private static final String OUTCOME = "VITTORIA";

    private StoryEndings() {
    }

    /**
     * Tutti i finali di vittoria. L'ordine conta: vince la prima variante soddisfatta.
     */
    public static EndingCatalog endings() {
        return new EndingCatalog(victory(), List.of(
                new EndingCatalog.Variant(
                        new AllOfCondition(List.of(
                                new AttributeCondition(Attribute.SAPIENZA, 2),
                                new AttributeCondition(Attribute.PERSUASIONE, 1))),
                        guardianVictory()),
                new EndingCatalog.Variant(
                        new FlagCondition(ForestDialogues.BELIEF_FLAG, ForestDialogues.BELIEF_ALDREN),
                        doubtVictory()),
                new EndingCatalog.Variant(
                        new AttributeCondition(Attribute.PERSUASIONE, 1),
                        persuasionVictory())));
    }

    /**
     * Finale predefinito, quello della trama.
     */
    public static EndingScreen victory() {
        return new EndingScreen(TITLE, HEADLINE, "HAI TROVATO ELARA",
                "Dopo anni di ricerca, Eren ha finalmente ritrovato sua madre e scoperto la verità sulla sua scomparsa.",
                OUTCOME);
    }

    private static EndingScreen guardianVictory() {
        return new EndingScreen(TITLE, HEADLINE, "HAI TROVATO ELARA E IL SEGRETO DEI CUSTODI",
                "Eren ha ritrovato sua madre e ha compreso il segreto dei Custodi della Luna: "
                        + "ora sa come proteggerlo, e non dovrà farlo da solo.",
                OUTCOME);
    }

    private static EndingScreen doubtVictory() {
        return new EndingScreen(TITLE, HEADLINE, "HAI TROVATO ELARA, NONOSTANTE I DUBBI",
                "Eren aveva creduto a chi negava la verità, ma ha seguito le tracce fino in fondo "
                        + "e ha ritrovato sua madre.",
                OUTCOME);
    }

    private static EndingScreen persuasionVictory() {
        return new EndingScreen(TITLE, HEADLINE, "HAI TROVATO ELARA CON LE PAROLE GIUSTE",
                "Eren ha ritrovato sua madre: lungo la strada, le parole giuste hanno aperto "
                        + "più porte di qualunque chiave.",
                OUTCOME);
    }
}