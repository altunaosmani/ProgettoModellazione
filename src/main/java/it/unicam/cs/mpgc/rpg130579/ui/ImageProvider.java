package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.image.Image;

import java.util.Optional;

/**
 * Contratto per il caricamento delle risorse grafiche associate agli elementi di gioco.
 */
public interface ImageProvider {
    Optional<Image> imageFor(ImageKind kind, String id);
}