package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.*;

import java.util.Optional;

public interface ProgressController {
    GameStatus status();
    ActionResult advance();
    Optional<EndingView> ending();
    IntroView intro();
    LevelInfoView levelInfo();
}
