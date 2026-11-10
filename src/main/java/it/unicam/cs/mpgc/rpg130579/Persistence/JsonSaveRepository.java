package it.unicam.cs.mpgc.rpg130579.Persistence;

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

public class JsonSaveRepository implements SaveRepository {
    private static final String DEFAULT_FILE = "savegame.json";

    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonSaveRepository() {
        this(Path.of(DEFAULT_FILE));
    }

    public JsonSaveRepository(Path path) {
        if (path == null) throw new IllegalArgumentException("Il percorso del file non può essere nullo");
        this.path = path;
    }

    @Override
    public void save(SaveData data) {
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        Path tmp = path.resolveSibling(path.getFileName() + ".tmp");
        try {
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
        if (!Files.exists(path)) return Optional.empty();
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
