package it.unicam.cs.mpgc.rpg130579.model.interaction;
import it.unicam.cs.mpgc.rpg130579.model.entity.EnigmaPieceItem;
import it.unicam.cs.mpgc.rpg130579.model.entity.Item;
import it.unicam.cs.mpgc.rpg130579.model.entity.Player;

import java.util.*;

public class SequencePuzzle extends AbstractPuzzle {
    private final List<EnigmaPieceItem> orderedPieces;

    public SequencePuzzle(String id, String description, Collection<EnigmaPieceItem> pieces) {
        super(id, description);
        if (pieces == null || pieces.size() < 2) throw new IllegalArgumentException("Servono almeno due frammenti");
        Set<Integer> positions = new HashSet<>();
        for (EnigmaPieceItem piece : pieces) {
            if (piece == null) throw new IllegalArgumentException("Il frammento non può essere nullo");
            if (!positions.add(piece.getOrderPosition())) throw new IllegalArgumentException("Due frammenti hanno la stessa posizione: " + piece.getOrderPosition());
        }
        List<EnigmaPieceItem> sorted = new ArrayList<>(pieces);
        sorted.sort(Comparator.comparingInt(EnigmaPieceItem::getOrderPosition));
        this.orderedPieces = Collections.unmodifiableList(sorted);
    }

    public int getPieceCount() {
        return orderedPieces.size();
    }

    @Override
    public boolean isSolvedBy(PuzzleAnswer answer, Player player) {
        if (answer == null || player == null) return false;
        if (answer.values().size() != orderedPieces.size()) return false;
        for (int i = 0; i < orderedPieces.size(); i++) {
            EnigmaPieceItem expected = orderedPieces.get(i);
            if (!player.hasItem(expected.getId())) return false;
            if (!expected.getId().equalsIgnoreCase(answer.values().get(i).trim())) return false;
        }
        return true;
    }

    @Override
    public int answerSize() { return orderedPieces.size(); }

    @Override
    public Map<String, String> choices(Player player) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Item item : player.getInventory()) {
            if (item instanceof EnigmaPieceItem piece && isPiece(piece)) {
                result.put(piece.getId(), piece.getPhraseFragment());
            }
        }
        return result;
    }

    private boolean isPiece(EnigmaPieceItem piece) {
        return orderedPieces.contains(piece);
    }
}
