package it.unicam.cs.mpgc.rpg130579.ui;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/** Cerca le immagini in src/main/resources/images/<cartella-del-tipo>/<id>.png */
public class ClasspathImageProvider implements ImageProvider {
    private final Map<String, Optional<Image>> cache = new HashMap<>();

    @Override
    public Optional<Image> imageFor(ImageKind kind, String id) {
        if (kind == null || id == null || id.isBlank()) return Optional.empty();
        return cache.computeIfAbsent(kind.folder() + "/" + id.toLowerCase(Locale.ROOT), key -> load(kind, key));
    }

    private Optional<Image> load(ImageKind kind, String key) {
        URL url = getClass().getResource("/images/" + key + ".png");
        if (url == null) return Optional.empty();
        double size = kind.loadSize();
        return Optional.of(new Image(url.toExternalForm(), size, size, true, true));
    }
}