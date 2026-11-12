package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.image.Image;

import java.util.Optional;

/** Fornisce l'immagine associata a un oggetto, se esiste. */
public interface ImageProvider {
    Optional<Image> imageForItem(String itemId);
}