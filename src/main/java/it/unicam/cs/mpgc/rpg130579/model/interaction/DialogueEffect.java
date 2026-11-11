package it.unicam.cs.mpgc.rpg130579.model.interaction;

import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;

@FunctionalInterface
public interface DialogueEffect {
    DialogueEffect NONE = progress -> { };

    void apply(StoryProgress progress);


}
