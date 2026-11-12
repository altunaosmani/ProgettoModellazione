package it.unicam.cs.mpgc.rpg130579.controller;

import it.unicam.cs.mpgc.rpg130579.view.ActionResult;
import it.unicam.cs.mpgc.rpg130579.view.ItemView;
import it.unicam.cs.mpgc.rpg130579.view.LocationView;

import java.util.List;

public class DefaultGameController implements GameController {
    private final GameManager manager;
    private final ViewMapper mapper = new ViewMapper();

    public DefaultGameController(GameManager manager) {
        if (manager == null) throw new IllegalArgumentException("Il game manager non può essere nullo");
        this.manager = manager;
    }

    @Override
    public LocationView currentLocation() {
        return mapper.toView(manager.getCurrentState().getPlayer().getCurrentLocation());
    }

    @Override
    public List<ItemView> inventory() {
        return mapper.toItemViews(manager.getCurrentState().getPlayer().getInventory());
    }

    @Override
    public ActionResult move(String exitId) {
        try {
            return manager.move(exitId) ? ActionResult.ok() : ActionResult.fail("Il passaggio è bloccato.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ActionResult.fail(e.getMessage());
        }
    }

    @Override
    public ActionResult takeItem(String itemId) {
        try {
            return manager.takeItem(itemId) ? ActionResult.ok("Oggetto raccolto.") : ActionResult.fail("Non puoi raccogliere questo oggetto.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ActionResult.fail(e.getMessage());
        }
    }

    @Override
    public boolean isGameOver() {
        return manager.isGameOver();
    }
}
