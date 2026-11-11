package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.interaction.Dialogue;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueNode;
import it.unicam.cs.mpgc.rpg130579.model.interaction.SetFlagEffect;

import java.util.List;

public final class ForestDialogues {
    private static final String BELIEF_FLAG = "credenza_elara";

    static Dialogue orwen() {
        return new Dialogue("Orwen", List.of(
                "Quel simbolo... lo conosco. Tua madre non era una semplice abitante di Tanusha.",
                "L'ultima volta che l'ho vista stava andando verso la Foresta dei Sussurri."));
    }

    static Dialogue mira() {
        return new Dialogue("Mira", List.of(
                "Eri così piccolo quando Elara se n'è andata... Eravamo amiche, sai?",
                "Non mi ha mai detto dove andava. Ma quella notte sembrava avere molta fretta."));
    }

    static Dialogue aldren() {
        DialogueNode start = new DialogueNode("start",
                List.of("Elara? Non voglio parlarne. E comunque non è mai entrata nella foresta.",
                        "Orwen racconta storie da vecchio. A chi credi, ragazzo?"),
                List.of(new DialogueChoice("Credo a Orwen", "credo_orwen",
                                new SetFlagEffect(BELIEF_FLAG, "orwen")),
                        new DialogueChoice("Credo ad Aldren", "credo_aldren",
                                new SetFlagEffect(BELIEF_FLAG, "aldren"))));
        DialogueNode believeOrwen = new DialogueNode("credo_orwen",
                List.of("Fa' come vuoi. Ma non dire che non ti avevo avvisato."));
        DialogueNode believeAldren = new DialogueNode("credo_aldren",
                List.of("Bene. Lascia stare queste storie e torna a casa."));
        return new Dialogue("Aldren", "start", List.of(start, believeOrwen, believeAldren));
    }

    private ForestDialogues() {
    }
}
