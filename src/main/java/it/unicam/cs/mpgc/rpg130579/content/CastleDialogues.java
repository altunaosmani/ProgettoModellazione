package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueNode;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SetFlagEffect;

import java.util.List;

public final class CastleDialogues {
    static final String ROWAN_HELP_FLAG = "rowan_aiuto";
    static final String ROWAN_HELP_VALUE = "si";

    /** Dialogo di default: Rowan non si fida di chi non ha il medaglione. */
    static Dialogue rowanStranger() {
        return new Dialogue("Rowan", List.of(
                "Chi sei? Il castello è abbandonato da anni e io non parlo con gli sconosciuti.",
                "Se hai qualcosa da mostrarmi, fallo. Altrimenti vattene."));
    }

    static Dialogue rowanRecognizes() {
        DialogueNode start = new DialogueNode("start",
                List.of("Quel medaglione... quell'occhio con i tre rami. Tu sei il figlio di Elara.",
                        "Tua madre è stata qui molti anni fa. Mi fece promettere di non raccontare "
                                + "a nessuno il suo segreto.",
                        "Mi dispiace, ragazzo. Una promessa è una promessa."),
                List.of(new DialogueChoice("Non sono uno sconosciuto. Sono suo figlio.", "aiuto",
                                new SetFlagEffect(ROWAN_HELP_FLAG, ROWAN_HELP_VALUE)),
                        new DialogueChoice("Capisco. Tornerò più tardi.", null)));
        DialogueNode help = new DialogueNode("aiuto",
                List.of("...Hai ragione. Non sei uno sconosciuto. Hai i suoi stessi occhi.",
                        "Se vuoi conoscere la verità, cerca nella biblioteca del castello un antico "
                                + "libro: \"I Custodi\".",
                        "La biblioteca è qui accanto, oltre il portone del cortile."));
        return new Dialogue("Rowan", "start", List.of(start, help));
    }

    private CastleDialogues() {
    }
}
