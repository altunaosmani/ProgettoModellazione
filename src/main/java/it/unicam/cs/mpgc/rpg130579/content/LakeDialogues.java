package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueNode;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SetFlagEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità contenente i dialoghi finali con Elara al Lago degli Specchi,
 * inclusa la variante attivata se il giocatore aveva dubitato di lei a Tanusha.
 */
public final class LakeDialogues {
    static final String ELARA_FOUND_FLAG = "elara_ritrovata";
    static final String ELARA_FOUND_VALUE = "si";

    /** Elara per chi aveva creduto a Orwen (o non aveva scelto). */
    static Dialogue elara() {
        return build(List.of("Eren... sei davvero tu."));
    }

    /** Elara per chi a Tanusha aveva creduto ad Aldren: stessa struttura, una battuta in più. */
    static Dialogue elaraAfterDoubt() {
        return build(List.of("Eren... sei davvero tu.",
                "So che al villaggio ti hanno detto che non ero mai entrata nella foresta, e che hai preferito crederlo. "
                        + "Non ti biasimo: era proprio quello che volevo che pensassero."));
    }

    private static Dialogue build(List<String> opening) {
        List<String> startLines = new ArrayList<>(opening);
        startLines.add("Non sono mai scomparsa perché avessi smesso di amarti.");
        startLines.add("Qualcuno voleva impossessarsi del segreto custodito dai Custodi della Luna. "
                + "Avevo paura che potessi diventare un bersaglio, così ho scelto di proteggerti "
                + "tenendomi lontana.");

        DialogueNode start = new DialogueNode("start", startLines,
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