package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.model.entity.Attribute;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.PuzzleAnswer;
import it.unicam.cs.mpgc.rpg130579.model.state.GameSetup;
import it.unicam.cs.mpgc.rpg130579.model.state.GameState;
import it.unicam.cs.mpgc.rpg130579.model.state.StoryProgress;
import it.unicam.cs.mpgc.rpg130579.persistence.GamePersistence;
import it.unicam.cs.mpgc.rpg130579.persistence.SaveRepository;
import it.unicam.cs.mpgc.rpg130579.persistence.SaveService;
import it.unicam.cs.mpgc.rpg130579.view.*;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Implementazione predefinita di {@link GameController}.
 * <p>
 * Coordina {@link GameManager}, {@link DialogueFlow}, {@link GamePersistence},
 * {@link ViewMapper} e {@link StatsChangeReporter}, traducendo le eccezioni di dominio
 * in {@link ActionResult} di errore e verificando il completamento delle missioni.
 */
public class DefaultGameController implements GameController {
    private final Supplier<GameSetup> newGameSource;
    private final GamePersistence persistence;
    private final ViewMapper mapper;
    private final StatsChangeReporter statsReporter;

    private GameSetup setup;
    private GameManager manager;
    private DialogueFlow dialogues;

    /**
     * Comodo: persistenza su repository, mapper e reporter predefiniti.
     */
    public DefaultGameController(Supplier<GameSetup> newGameSource, SaveRepository repository) {
        this(newGameSource, new SaveService(repository), new DefaultViewMapper(), new StatsChangeReporter());
    }

    /**
     * Crea un controller configurando esplicitamente tutte le sue dipendenze.
     *
     * @param newGameSource fornitore di nuove configurazioni di partita
     * @param persistence componente per il salvataggio e caricamento dello stato
     * @param mapper convertitore dalle entità di dominio ai DTO della vista
     * @param statsReporter componente per il calcolo e la formattazione dei guadagni negli attributi
     * @throws IllegalArgumentException se uno qualsiasi dei parametri è {@code null}
     */
    public DefaultGameController(Supplier<GameSetup> newGameSource, GamePersistence persistence,
                                 ViewMapper mapper, StatsChangeReporter statsReporter) {
        if (newGameSource == null) throw new IllegalArgumentException("La sorgente di partite non può essere nulla");
        if (persistence == null) throw new IllegalArgumentException("La persistenza non può essere nulla");
        if (mapper == null) throw new IllegalArgumentException("Il mapper non può essere nullo");
        if (statsReporter == null)
            throw new IllegalArgumentException("Il reporter delle statistiche non può essere nullo");
        this.newGameSource = newGameSource;
        this.persistence = persistence;
        this.mapper = mapper;
        this.statsReporter = statsReporter;
        adopt(newGameSource.get());
    }

    private void adopt(GameSetup newSetup) {
        this.setup = newSetup;
        this.manager = new GameManager(newSetup.state());
        this.dialogues = new DialogueFlow(manager, statsReporter);
    }

    private Player player() {
        return manager.getCurrentState().getPlayer();
    }

    private StoryProgress progress() {
        return manager.getCurrentState().getProgress();
    }

    /**
     * Dopo ogni azione che può cambiare lo stato della storia controlla se la missione è finita.
     */
    private String questNote() {
        return manager.completeCurrentQuest() ? " Missione completata!" : "";
    }

    // ---------- Esplorazione ----------

    @Override
    public LocationView currentLocation() {
        return mapper.toView(player().getCurrentLocation(), player(), progress());
    }

    @Override
    public List<ItemView> inventory() {
        return mapper.toItemViews(player().getInventory());
    }

    @Override
    public ActionResult move(String exitId) {
        try {
            return manager.move(exitId) ? ActionResult.ok() : ActionResult.fail("Il passaggio è bloccato.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    @Override
    public ActionResult takeItem(String itemId) {
        try {
            Map<Attribute, Integer> before = statsReporter.capture(player());
            if (!manager.takeItem(itemId)) return ActionResult.fail("Non puoi raccogliere questo oggetto.");
            return ActionResult.ok("Oggetto raccolto." + statsReporter.describeGains(player(), before) + questNote());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    @Override
    public ActionResult unlock(String mechanismId, List<String> answer) {
        try {
            Map<Attribute, Integer> before = statsReporter.capture(player());
            PuzzleAnswer given = (answer == null || answer.isEmpty()) ? PuzzleAnswer.empty() : new PuzzleAnswer(answer);
            if (!manager.unlockMechanism(mechanismId, given)) {
                return ActionResult.fail("Non succede nulla. Forse ti manca qualcosa, o la scelta non è quella giusta.");
            }
            return ActionResult.ok("Si è sbloccato qualcosa!" + statsReporter.describeGains(player(), before) + questNote());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    // ---------- Dialoghi (delegati a DialogueFlow) ----------

    @Override
    public DialogueView talkTo(String npcId) {
        return dialogues.talkTo(npcId);
    }

    @Override
    public DialogueView choose(int index) {
        return dialogues.choose(index);
    }

    @Override
    public ActionResult endDialogue() {
        return dialogues.endDialogue();
    }

    // ---------- Personaggio e progressione ----------

    @Override
    public StatsView stats() {
        return mapper.toView(player());
    }

    @Override
    public GameStatus status() {
        GameState state = manager.getCurrentState();
        boolean completed = !state.isGameEnd() && state.getCurrentLevel().isCompleted();
        return new GameStatus(completed, state.isGameEnd(), state.isWin());
    }

    @Override
    public ActionResult advance() {
        try {
            manager.advanceToNextLevel();
            return manager.isGameOver()
                    ? ActionResult.ok("Hai completato il gioco!")
                    : ActionResult.ok(manager.getCurrentLevel().getName());
        } catch (IllegalStateException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    /**
     * Il finale dipende dalle scelte fatte: lo seleziona il catalogo dei finali sullo stato finale della partita.
     */
    @Override
    public Optional<EndingView> ending() {
        if (!manager.hasWon()) return Optional.empty();
        return Optional.of(mapper.toView(setup.endings().select(player(), progress())));
    }

    @Override
    public IntroView intro() {
        return mapper.toView(setup.introScreen());
    }

    @Override
    public LevelInfoView levelInfo() {
        GameState state = manager.getCurrentState();
        return mapper.toView(state.getCurrentLevel(), state.getCurrentLevelIndex() + 1,
                state.getLevelManager().getLevelsSize());
    }

    // ---------- Persistenza ----------

    @Override
    public boolean hasSave() {
        return persistence.hasSave();
    }

    @Override
    public ActionResult save() {
        try {
            persistence.save(manager.getCurrentState());
            return ActionResult.ok("Partita salvata.");
        } catch (UncheckedIOException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    @Override
    public ActionResult load() {
        GameSetup fresh = newGameSource.get(); // stato e catalogo devono venire dalla stessa partita
        try {
            if (!persistence.load(fresh.state(), fresh.catalog()))
                return ActionResult.fail("Nessun salvataggio trovato.");
            adopt(fresh);
            return ActionResult.ok("Partita caricata.");
        } catch (IllegalStateException | UncheckedIOException e) {
            return ActionResult.fail(ErrorMessages.of(e));
        }
    }

    @Override
    public void newGame() {
        adopt(newGameSource.get());
    }
}