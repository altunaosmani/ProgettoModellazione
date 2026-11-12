package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.DialogueView;

public interface DialogueController {
    DialogueView talkTo(String npcId);
    DialogueView choose(int index);
}
