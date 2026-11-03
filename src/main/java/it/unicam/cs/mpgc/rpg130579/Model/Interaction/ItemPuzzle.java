package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import it.unicam.cs.mpgc.rpg130579.Model.Entity.Player;

public class ItemPuzzle extends AbstractPuzzle {
    private final String requiredItemId;

    public ItemPuzzle(String id, String description, String requiredItemId) {
        super(id, description);
        if (requiredItemId == null || requiredItemId.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto richiesto non può essere vuoto");
        this.requiredItemId = requiredItemId;
    }

    public String getRequiredItemId() {
        return this.requiredItemId;
    }

    @Override
    public boolean isSolvedBy(PuzzleAnswer answer, Player player) {
        return player != null && player.hasItem(requiredItemId);
    }
}
