package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueChoice;
import it.unicam.cs.mpgc.rpg130579.model.interaction.DialogueSession;
import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.DialogueView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Gestisce la conversazione in corso con un NPC: sessione, attributi guadagnati
 * e completamento della missione durante il dialogo.
 */
final class DialogueFlow implements DialogueController {
    private final GameSessionService sessionService;
    private final StatsTracker statsTracker;
    private DialogueSession session;
    private Map<Attribute, Integer> statsAtStart;
    private boolean questCompleted;

    DialogueFlow(GameSessionService sessionService, StatsTracker statsTracker) {
        this.sessionService = Objects.requireNonNull(sessionService, "Il servizio di sessione non può essere nullo");
        this.statsTracker = Objects.requireNonNull(statsTracker, "Il tracker non può essere nullo");
    }

    @Override
    public DialogueView talkTo(String npcId) {
        try {
            session = sessionService.interactWithNPC(npcId);
            statsAtStart = statsTracker.capture(player());
            questCompleted = false;
            return snapshot();
        } catch (IllegalArgumentException | IllegalStateException e) {
            reset();
            return DialogueView.error(ErrorMessages.of(e));
        }
    }

    @Override
    public DialogueView choose(int index) {
        if (session == null) return DialogueView.error("Nessun dialogo in corso.");
        try {
            session.choose(index);
            return snapshot();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return DialogueView.error(ErrorMessages.of(e));
        }
    }

    @Override
    public ActionResult endDialogue() {
        if (sessionService.completeCurrentQuest()) questCompleted = true;
        String note = statsAtStart == null
                ? ""
                : statsTracker.describeGains(player(), statsAtStart).trim();
        if (questCompleted) note = (note.isEmpty() ? "" : note + " ") + "Missione completata!";
        reset();
        return ActionResult.ok(note);
    }

    private DialogueView snapshot() {
        List<String> lines = new ArrayList<>();
        while (session.hasNextLine()) lines.add(session.nextLine());
        List<String> choices = session.awaitsChoice()
                ? session.getChoices().stream().map(DialogueChoice::text).toList()
                : List.of();
        DialogueView view = new DialogueView(session.getTitle(), lines, choices, session.isFinished());
        if (view.finished()) {
            session = null;
            if (sessionService.completeCurrentQuest()) questCompleted = true;
        }
        return view;
    }

    private Player player() {
        return sessionService.getCurrentState().getPlayer();
    }

    private void reset() {
        session = null;
        statsAtStart = null;
        questCompleted = false;
    }
}