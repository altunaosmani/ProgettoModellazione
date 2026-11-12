package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import it.unicam.cs.mpgc.rpg130579.view.GameStatus;

import java.util.Optional;

public interface ProgressController {
    GameStatus status();
    ActionResult advance();
    Optional<EndingView> ending();
}
