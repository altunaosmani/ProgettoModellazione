package it.unicam.cs.mpgc.rpg130579.Persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class JsonFileSaveRepository implements SaveRepository {
    private final Path path;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonFileSaveRepository(Path path) {
        if (path == null) throw new IllegalArgumentException("Il percorso file non può essere nullo");
        this.path = path;
    }

    public JsonFileSaveRepository() {
        this(Path.of("savegame.json"));
    }

    @Override
    public void save(SaveData data) {
        if (data == null) throw new IllegalArgumentException("Il salvataggio non può essere nullo");
        try {
            Files.writeString(path, gson.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Errore nel salvataggio della partita", e);
        }
    }

    @Override
    public Optional<SaveData> load() {
        if (!Files.exists(path)) return Optional.empty();
        try {
            String json = Files.readString(path, StandardCharsets.UTF_8);
            return Optional.ofNullable(gson.fromJson(json, SaveData.class));
        } catch (IOException e) {
            throw new UncheckedIOException("Errore nella lettura del salvataggio", e);
        } catch (JsonParseException e) {
            throw new IllegalStateException("Il file di salvataggio è corrotto", e);
        }
    }
}