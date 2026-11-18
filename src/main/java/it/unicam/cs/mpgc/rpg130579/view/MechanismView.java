package it.unicam.cs.mpgc.rpg130579.view;

/**
 * Dati di presentazione di un meccanismo, dell'eventuale enigma e dell'indizio visibile.
 */
public record MechanismView(String id, String name, String description, boolean locked, PuzzleView puzzle,
                            String hint) {
    public MechanismView {
        hint = hint == null ? "" : hint;
    }
}