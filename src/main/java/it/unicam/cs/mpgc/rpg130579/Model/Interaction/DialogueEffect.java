package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.State.StoryProgress;

@FunctionalInterface
public interface DialogueEffect {
    DialogueEffect NONE = progress -> { };

    void apply(StoryProgress progress);


}
