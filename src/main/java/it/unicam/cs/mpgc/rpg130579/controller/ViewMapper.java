package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.view.*;

import java.util.List;

/**
 * Contratto per la conversione delle entità del modello nei DTO immutabili destinati alla UI.
 * Consente di sostituire la rappresentazione della vista senza alterare la logica di gioco.
 */
public interface ViewMapper {
    LocationView toView(Location location, Player player, StoryProgress progress);

    List<ItemView> toItemViews(List<Item> items);

    StatsView toView(Player player);

    IntroView toView(IntroScreen intro);

    LevelInfoView toView(Level level, int number, int total);

    EndingView toView(EndingScreen ending);
}