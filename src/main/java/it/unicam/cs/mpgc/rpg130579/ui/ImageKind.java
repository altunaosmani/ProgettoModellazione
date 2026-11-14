package it.unicam.cs.mpgc.rpg130579.ui;

public enum ImageKind {
    ITEM("items", 128),
    NPC("npcs", 128),
    LOCATION("locations", 128);

    private final String folder;
    private final double loadSize;

    ImageKind(String folder, double loadSize) {
        this.folder = folder;
        this.loadSize = loadSize;
    }

    public String folder() { return folder; }
    public double loadSize() { return loadSize; }
}
