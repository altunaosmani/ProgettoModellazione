package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Cerca le immagini in src/main/resources/images/items/<id>.png */
public class ClasspathImageProvider implements ImageProvider {
    private static final String BASE_PATH = "/images/items/";
    private static final double LOAD_SIZE = 128;

    private final Map<String, Optional<Image>> cache = new HashMap<>();

    @Override
    public Optional<Image> imageForItem(String itemId) {
        if (itemId == null || itemId.isBlank()) return Optional.empty();
        return cache.computeIfAbsent(itemId.toLowerCase(Locale.ROOT), this::load);
    }

    private Optional<Image> load(String id) {
        URL url = getClass().getResource(BASE_PATH + id + ".png");
        if (url == null) return Optional.empty();
        return Optional.of(new Image(url.toExternalForm(), LOAD_SIZE, LOAD_SIZE, true, true));
    }
}