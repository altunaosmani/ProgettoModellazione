package it.unicam.cs.mpgc.rpg130579.view;

/** hint: indizio visibile al giocatore in questo momento; stringa vuota se non ce n'è. */
public record MechanismView(String id, String name, String description, boolean locked, PuzzleView puzzle, String hint) {
    public MechanismView {
        hint = hint == null ? "" : hint;
    }
}