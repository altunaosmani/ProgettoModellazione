package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.image.Image;

import java.util.Optional;

/**
 * Contratto per il caricamento delle risorse grafiche associate agli elementi di gioco.
 */
public interface ImageProvider {
    /**
     * Restituisce l'immagine associata a un elemento di gioco, se disponibile.
     *
     * @param kind categoria dell'immagine (oggetto, NPC o luogo)
     * @param id identificativo dell'elemento
     * @return {@link Optional} contenente l'immagine caricata, oppure vuoto se assente
     */
    Optional<Image> imageFor(ImageKind kind, String id);
}