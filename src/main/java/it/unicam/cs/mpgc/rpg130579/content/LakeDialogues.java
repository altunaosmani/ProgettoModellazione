package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueNode;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SetFlagEffect;

import java.util.List;

public final class LakeDialogues {
    static final String ELARA_FOUND_FLAG = "elara_ritrovata";
    static final String ELARA_FOUND_VALUE = "si";

    static Dialogue elara() {
        DialogueNode start = new DialogueNode("start",
                List.of("Eren... sei davvero tu.",
                        "Non sono mai scomparsa perché avessi smesso di amarti.",
                        "Qualcuno voleva impossessarsi del segreto custodito dai Custodi della Luna. "
                                + "Avevo paura che potessi diventare un bersaglio, così ho scelto di proteggerti "
                                + "tenendomi lontana."),
                List.of(new DialogueChoice("Perché non sei mai tornata?", "perche")));

        DialogueNode why = new DialogueNode("perche",
                List.of("Credevo che proteggerti significasse lasciarti andare."),
                List.of(new DialogueChoice("Avrei preferito conoscere la verità, invece di crescere "
                                + "pensando di essere stato abbandonato.", "errore"),
                        new DialogueChoice("Ti ho cercata per anni.", "errore")));

        DialogueNode mistake = new DialogueNode("errore",
                List.of("Hai ragione. Ho commesso un errore: avrei dovuto fidarmi di te.",
                        "Ora non devi più cercarmi."),
                List.of(new DialogueChoice("Ora siamo di nuovo insieme.", null,
                        new SetFlagEffect(ELARA_FOUND_FLAG, ELARA_FOUND_VALUE))));

        return new Dialogue("Elara", "start", List.of(start, why, mistake));
    }

    private LakeDialogues() {
    }
}

