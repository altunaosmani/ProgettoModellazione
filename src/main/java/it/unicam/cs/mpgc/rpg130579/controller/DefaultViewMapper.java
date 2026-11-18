package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.NPC;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ConditionalText;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Puzzle;
import it.unicam.cs.mpgc.rpg130579.model.interaction.Quest;
import it.unicam.cs.mpgc.rpg130579.model.state.EndingScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.IntroScreen;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.model.world.Exit;
import it.unicam.cs.mpgc.rpg130579.model.world.Level;
import it.unicam.cs.mpgc.rpg130579.model.world.Location;
import it.unicam.cs.mpgc.rpg130579.model.world.Mechanism;
import it.unicam.cs.mpgc.rpg130579.view.EndingView;
import it.unicam.cs.mpgc.rpg130579.view.ExitView;
import it.unicam.cs.mpgc.rpg130579.view.IntroView;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LevelInfoView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;
import it.unicam.cs.mpgc.rpg130579.view.MechanismView;
import it.unicam.cs.mpgc.rpg130579.view.NpcView;
import it.unicam.cs.mpgc.rpg130579.view.PuzzleView;
import it.unicam.cs.mpgc.rpg130579.view.StatsView;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementazione standard di {@link ViewMapper} che filtra i contenuti condizionali
 * (dettagli dei luoghi e indizi dei meccanismi) in base agli attributi e ai progressi del giocatore.
 */
public class DefaultViewMapper implements ViewMapper {

    @Override
    public LocationView toView(Location location, Player player, StoryProgress progress) {
        List<String> details = location.getDetails().stream()
                .filter(d -> d.isVisibleTo(player, progress))
                .map(ConditionalText::text)
                .toList();
        return new LocationView(
                location.getId(),
                location.getName(),
                location.getDescription(),
                location.getItems().stream().map(this::itemView).toList(),
                location.getNpcs().stream().map(this::npcView).toList(),
                location.getExits().stream().map(this::exitView).toList(),
                location.getMechanisms().stream().map(m -> mechanismView(m, player, progress)).toList(),
                details);
    }

    @Override
    public List<ItemView> toItemViews(List<Item> items) {
        return items.stream().map(this::itemView).toList();
    }

    @Override
    public StatsView toView(Player player) {
        Map<String, Integer> values = new LinkedHashMap<>();
        Map<String, Integer> bonuses = new LinkedHashMap<>();
        for (Attribute attribute : Attribute.values()) {
            values.put(attribute.getLabel(), player.getAttributeValue(attribute));
            bonuses.put(attribute.getLabel(), player.getAttributeBonus(attribute));
        }
        return new StatsView(values, bonuses);
    }

    @Override
    public IntroView toView(IntroScreen intro) {
        return new IntroView(intro.title(), intro.paragraphs());
    }

    @Override
    public LevelInfoView toView(Level level, int number, int total) {
        Quest quest = level.getQuest();
        return new LevelInfoView(number, total, level.getName(), level.getDescription(),
                quest.getName(), quest.getDescription(), quest.isCompleted());
    }

    @Override
    public EndingView toView(EndingScreen ending) {
        return new EndingView(ending.gameTitle(), ending.headline(), ending.subtitle(), ending.message(), ending.outcome());
    }

    // ---------- Elementi interni ----------

    private ItemView itemView(Item item) {
        return new ItemView(item.getId(), item.getName(), item.getDescription(), item.examine());
    }

    private ExitView exitView(Exit exit) {
        return new ExitView(exit.getId(), exit.getLabel(), exit.isPassable());
    }

    private NpcView npcView(NPC npc) {
        return new NpcView(npc.getId(), npc.getName(), npc.getDescription());
    }

    private PuzzleView puzzleView(Puzzle puzzle, Player player) {
        return new PuzzleView(puzzle.getDescription(), puzzle.choices(player), puzzle.answerSize());
    }

    private MechanismView mechanismView(Mechanism mechanism, Player player, StoryProgress progress) {
        PuzzleView puzzle = mechanism.getRequiredPuzzle()
                .map(p -> puzzleView(p, player))
                .orElse(null);
        String hint = mechanism.getHint()
                .filter(h -> h.isVisibleTo(player, progress))
                .map(ConditionalText::text)
                .orElse("");
        return new MechanismView(mechanism.getId(), mechanism.getName(),
                mechanism.getDescription(), mechanism.isLocked(), puzzle, hint);
    }
}