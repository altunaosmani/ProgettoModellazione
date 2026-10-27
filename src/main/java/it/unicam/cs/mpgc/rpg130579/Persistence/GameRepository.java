package it.unicam.cs.mpgc.rpg130579.Persistence;

import java.io.*;

public class GameRepository {
    private final String filePath;

    public GameRepository(String filePath) {
        if (filePath == null || filePath.isBlank()) throw new IllegalArgumentException("Il percorso file non può essere vuoto");
        this.filePath = filePath;
    }

    public GameRepository() {
        this("savegame.dat");
    }

    public boolean saveGame(SaveData data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
            return true;
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio della partita: " + e.getMessage());
            return false;
        }
    }

    public SaveData loadGame() {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (SaveData) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore durante il caricamento della partita: " + e.getMessage());
            return null;
        }
    }
}
