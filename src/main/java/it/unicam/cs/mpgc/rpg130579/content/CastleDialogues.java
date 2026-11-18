package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.interaction.AttributeCondition;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueNode;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SetFlagEffect;

import java.util.List;

/**
 * Classe di utilità contenente i dialoghi del guardiano Rowan nel Castello di Ardan
 * e le costanti per tracciare la sua disponibilità ad aiutare il giocatore.
 */
public final class CastleDialogues {
    static final String ROWAN_HELP_FLAG = "rowan_aiuto";
    static final String ROWAN_HELP_VALUE = "si";

    /**
     * Primo incontro: Rowan riconosce il medaglione e all'inizio esita.
     * Con almeno 1 in Persuasione compare una scelta in più. Resta sempre l'ultima,
     * così gli indici delle altre scelte non cambiano.
     */
    static Dialogue rowanFirstMeeting() {
        SetFlagEffect convinced = new SetFlagEffect(ROWAN_HELP_FLAG, ROWAN_HELP_VALUE);
        DialogueNode start = new DialogueNode("start",
                List.of("Fermo! ...Quel medaglione. Quell'occhio con i tre rami. Tu sei il figlio di Elara.",
                        "Tua madre è stata qui molti anni fa. Mi fece promettere di non raccontare a nessuno il suo segreto.",
                        "Mi dispiace, ragazzo. Una promessa è una promessa."),
                List.of(new DialogueChoice("Non sono uno sconosciuto. Sono suo figlio.", "aiuto", convinced),
                        new DialogueChoice("Capisco. Tornerò più tardi.", null),
                        new DialogueChoice("[Persuasione] Mia madre avrebbe voluto che io sapessi la verità. E tu lo sai.",
                                "aiuto", convinced, new AttributeCondition(Attribute.PERSUASIONE, 1))));
        DialogueNode help = new DialogueNode("aiuto",
                List.of("...Hai ragione. Non sei uno sconosciuto. Hai i suoi stessi occhi.",
                        "Se vuoi conoscere la verità, cerca nella biblioteca del castello un antico libro: \"I Custodi\".",
                        "La biblioteca è qui accanto, oltre il portone del cortile."));
        return new Dialogue("Rowan", "start", List.of(start, help));
    }

    /** Dopo che Rowan ha scelto di aiutare, ripete solo l'indicazione. */
    static Dialogue rowanReminder() {
        return new Dialogue("Rowan", List.of(
                "Cerca nella biblioteca il libro \"I Custodi\". È qui accanto, oltre il portone del cortile.",
                "Tua madre sarebbe fiera di te, Eren."));
    }

    private CastleDialogues() {
    }
}