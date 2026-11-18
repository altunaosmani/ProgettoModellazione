package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.interaction.*;

import java.util.List;

/**
 * Classe di utilità contenente le definizioni statiche dei dialoghi degli NPC
 * del primo livello (Orwen, Mira, Aldren) e le relative chiavi dei flag narrativi.
 */
public final class ForestDialogues {
    /**
     * A chi crede il giocatore tra Orwen e Aldren: letto nel dialogo finale con Elara.
     */
    static final String BELIEF_FLAG = "credenza_elara";
    static final String BELIEF_ORWEN = "orwen";
    static final String BELIEF_ALDREN = "aldren";

    /**
     * Segnano che il dialogo "con ricompensa" è già stato fatto, così il bonus non si ripete.
     */
    static final String MIRA_TALKED_FLAG = "mira_confidenza";
    static final String ALDREN_TALKED_FLAG = "aldren_parlato";
    static final String DONE = "si";

    private ForestDialogues() {
    }

    static Dialogue orwen() {
        return new Dialogue("Orwen", List.of(
                "Quel simbolo... lo conosco. Tua madre non era una semplice abitante di Tanusha.", "L'ultima volta che l'ho vista stava andando verso la Foresta dei Sussurri."));
    }

    static Dialogue mira() {
        DialogueNode start = new DialogueNode("start",
                List.of("Eri così piccolo quando Elara se n'è andata... Eravamo amiche, sai?",
                        "Non mi ha mai detto dove andava. Ma quella notte sembrava avere molta fretta."),
                List.of(new DialogueChoice("Ti prego, Mira: mi basta qualunque ricordo di lei.", "ricordo",
                                CompositeEffect.of(new SetFlagEffect(MIRA_TALKED_FLAG, DONE),
                                        new ModifyAttributeEffect(Attribute.PERSUASIONE, 1))),
                        new DialogueChoice("Grazie, Mira.", null)));
        DialogueNode memory = new DialogueNode("ricordo",
                List.of("...Va bene. Prima di andarsene mi disse soltanto: \"Se qualcuno mi cerca, di' che non sono mai stata qui.\"",
                        "Allora non capii. Forse ora sì."));
        return new Dialogue("Mira", "start", List.of(start, memory));
    }

    /**
     * Dopo aver ascoltato il ricordo di Mira.
     */
    static Dialogue miraAfter() {
        return new Dialogue("Mira", List.of("Ti ho detto tutto quello che so, Eren. Abbi cura di te."));
    }

    static Dialogue aldren() {
        DialogueNode start = new DialogueNode("start",
                List.of("Elara? Non voglio parlarne. E comunque non è mai entrata nella foresta.",
                        "Orwen racconta storie da vecchio. A chi credi, ragazzo?"),
                List.of(new DialogueChoice("Credo a Orwen", "credo_orwen",
                                CompositeEffect.of(new SetFlagEffect(BELIEF_FLAG, BELIEF_ORWEN),
                                        new SetFlagEffect(ALDREN_TALKED_FLAG, DONE),
                                        new ModifyAttributeEffect(Attribute.SAPIENZA, 1))),
                        new DialogueChoice("Credo ad Aldren", "credo_aldren",
                                CompositeEffect.of(new SetFlagEffect(BELIEF_FLAG, BELIEF_ALDREN),
                                        new SetFlagEffect(ALDREN_TALKED_FLAG, DONE),
                                        new ModifyAttributeEffect(Attribute.PERCEZIONE, 1)))));
        DialogueNode believeOrwen = new DialogueNode("credo_orwen", List.of("Fa' come vuoi. Ma non dire che non ti avevo avvisato."));
        DialogueNode believeAldren = new DialogueNode("credo_aldren", List.of("Bene. Lascia stare queste storie e torna a casa."));
        return new Dialogue("Aldren", "start", List.of(start, believeOrwen, believeAldren));
    }

    /**
     * Dopo che il giocatore ha scelto a chi credere.
     */
    static Dialogue aldrenAfter() {
        return new Dialogue("Aldren", List.of("Ho già detto quello che dovevo, ragazzo. Non insistere."));
    }
}