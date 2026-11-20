package it.unicam.cs.mpgc.rpg130579.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

/**
 * Implementazione di {@link SaveRepository} su file JSON tramite libreria Gson.
 */
public class JsonSaveRepository implements SaveRepository {
    private static final Path DEFAULT_PATH = Path.of(
            System.getProperty("user.home"), ".the-lost-elf", "savegame.json");

    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonSaveRepository() {
        this(DEFAULT_PATH);
    }

    public JsonSaveRepository(Path path) {
        if (path == null) throw new IllegalArgumentException("Il percorso del file non può essere nullo");
        this.path = path;
    }

    @Override
    public boolean exists() {
        return Files.isRegularFile(path);
    }

    @Override
    public void save(SaveData data) {
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        try {
            Path parent = path.getParent();
            if (parent != null) Files.createDirectories(parent);

            Path tmp = path.resolveSibling(path.getFileName() + ".tmp");
            Files.writeString(tmp, gson.toJson(data), StandardCharsets.UTF_8);
            try {
                Files.move(tmp, path, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Errore nel salvataggio della partita", e);
        }
    }

    @Override
    public Optional<SaveData> load() {
        if (!exists()) return Optional.empty();
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            SaveData data = gson.fromJson(json, SaveData.class);
            if (data == null) throw new IOException("Il file di salvataggio è vuoto");
            return Optional.of(data);
        } catch (IOException e) {
            throw new UncheckedIOException("Errore nel caricamento della partita", e);
        } catch (JsonParseException e) {
            throw new UncheckedIOException(new IOException("Il file di salvataggio è corrotto", e));
        }
    }
}