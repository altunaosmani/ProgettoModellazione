package it.unicam.cs.mpgc.rpg130579.content;

import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.KeyItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;
import it.unicam.cs.mpgc.rpg130579.model.interaction.ItemPuzzle;
import it.unicam.cs.mpgc.rpg130579.model.state.GameSetup;
import it.unicam.cs.mpgc.rpg130579.model.world.*;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Controlla la coerenza strutturale del mondo costruito da {@link GameFactory},
 * così un errore di contenuto emerge nei test e non giocando.
 */
class WorldValidationTest {
    private final GameSetup setup = new GameFactory().newGame();
    private final LevelManager levels = setup.state().getLevelManager();

    private List<Item> itemsInWorld() {
        List<Item> all = new ArrayList<>(setup.state().getPlayer().getInventory());
        for (Level level : levels.getLevels())
            for (Location place : level.getPlaces()) all.addAll(place.getItems());
        return all;
    }

    @Test
    void everyExitLeadsToAnExistingPlaceOfTheSameLevel() {
        for (Level level : levels.getLevels())
            for (Location place : level.getPlaces())
                for (Exit exit : place.getExits())
                    assertTrue(level.findPlaceById(exit.getDestinationLocationId()).isPresent(),
                            level.getId() + "/" + place.getId() + ": uscita '" + exit.getId()
                                    + "' verso luogo inesistente " + exit.getDestinationLocationId());
    }

    @Test
    void everyBarrierIsAMechanismOfThePlaceWhereTheExitIs() {
        // unlockMechanism() cerca il meccanismo nel luogo corrente: la barriera deve stare lì
        for (Level level : levels.getLevels())
            for (Location place : level.getPlaces())
                for (Exit exit : place.getExits()) {
                    Lockable barrier = exit.getBarrier();
                    if (barrier == Lockable.NEVER_LOCKED) continue;
                    assertTrue(place.getMechanisms().contains(barrier),
                            place.getId() + ": la barriera dell'uscita '" + exit.getId()
                                    + "' non è un meccanismo di questo luogo");
                }
    }

    @Test
    void everyMechanismIsInExactlyOnePlaceOfItsLevel() {
        for (Level level : levels.getLevels()) {
            for (Mechanism mechanism : level.getMechanisms()) {
                long count = level.getPlaces().stream()
                        .filter(p -> p.getMechanisms().contains(mechanism)).count();
                assertEquals(1, count, "Meccanismo " + mechanism.getId() + " in " + count + " luoghi");
            }
            for (Location place : level.getPlaces())
                for (Mechanism mechanism : place.getMechanisms())
                    assertTrue(level.getMechanisms().contains(mechanism),
                            "Meccanismo non registrato nel livello: " + mechanism.getId());
        }
    }

    @Test
    void everyPlaceIsReachableFromTheLevelStart() {
        // raggiungibilità ottimistica: si ignorano le barriere, quelle le copre il test di percorso
        for (Level level : levels.getLevels()) {
            Location start = level.getStartLocation();
            Set<Location> seen = new HashSet<>(Set.of(start));
            Deque<Location> todo = new ArrayDeque<>(List.of(start));
            while (!todo.isEmpty()) {
                for (Exit exit : todo.pop().getExits()) {
                    Location next = level.getPlaceById(exit.getDestinationLocationId());
                    if (seen.add(next)) todo.push(next);
                }
            }
            assertEquals(level.getPlaces().size(), seen.size(), "Luoghi irraggiungibili in " + level.getId());
        }
    }

    @Test
    void everyItemIsRegisteredAndEveryRegisteredItemIsPlaced() {
        List<Item> all = itemsInWorld();
        Set<Item> unique = new HashSet<>(all);
        assertEquals(all.size(), unique.size(), "Un oggetto compare in più posti");
        for (Item item : all)
            assertTrue(setup.catalog().findById(item.getId()).isPresent(), "Non nel catalogo: " + item.getId());
        assertEquals(setup.catalog().size(), unique.size(), "Oggetti nel catalogo ma non nel mondo");
    }

    @Test
    void allPortableItemsFitInTheInventory() {
        long portable = itemsInWorld().stream().filter(Item::isPortable).count();
        assertTrue(portable < Player.MAX_CAPACITY,
                portable + " oggetti raccoglibili: serve almeno un posto libero di margine");
    }

    @Test
    void everyKeyOpensAMechanismAndIsNotFoundAfterIt() {
        List<Level> all = levels.getLevels();
        for (int i = 0; i < all.size(); i++)
            for (Location place : all.get(i).getPlaces())
                for (Item item : place.getItems()) {
                    if (!(item instanceof KeyItem key)) continue;
                    int gateLevel = -1;
                    for (int j = 0; j < all.size(); j++)
                        if (all.get(j).findMechanismById(key.getTargetMechanismId()).isPresent()) gateLevel = j;
                    assertTrue(gateLevel >= 0, "Chiave senza meccanismo: " + key.getId());
                    assertTrue(gateLevel >= i, "Chiave trovata dopo il suo cancello: " + key.getId());
                }
    }

    @Test
    void itemPuzzlesRequireExistingItems() {
        for (Level level : levels.getLevels())
            for (Mechanism mechanism : level.getMechanisms())
                mechanism.getRequiredPuzzle()
                        .filter(ItemPuzzle.class::isInstance)
                        .map(ItemPuzzle.class::cast)
                        .ifPresent(p -> assertTrue(setup.catalog().findById(p.getRequiredItemId()).isPresent(),
                                "Enigma con oggetto inesistente: " + p.getRequiredItemId()));
    }
}