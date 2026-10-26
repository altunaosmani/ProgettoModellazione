package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

public class Enigma {
    private final String id;
    private final String description;
    private final String solution;
    private boolean solved;

    public Enigma(String id, String description, String solution) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'ID dell'enigma non deve essere vuoto");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("La descrizione dell'enigma non può essere vuota");
        if (solution == null || solution.isBlank()) throw new IllegalArgumentException("La soluzione dell'enigma non può essere vuota");
        this.id = id;
        this.description = description;
        this.solution = solution;
        this.solved = false;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSolution() {
        return this.solution;
    }

    public boolean isSolved() {
        return this.solved;
    }

    public boolean checkAnswer(String answer) {
        if (answer == null || answer.isBlank()) return false;
        if (solved) return true;
        if (solution.equalsIgnoreCase(answer.trim())) {
            solved = true;
            return true;
        }
        return false;
    }
}
