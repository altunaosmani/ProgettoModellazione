package it.unicam.cs.mpgc.rpg130579.view;

/**
 * Dati informativi sul livello corrente e sulla missione associata.
 */
public record LevelInfoView(int number, int total, String name, String description, String questName,
                            String questDescription, boolean questCompleted) {
}

