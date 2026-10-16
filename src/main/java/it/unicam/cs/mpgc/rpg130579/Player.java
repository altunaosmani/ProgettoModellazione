package it.unicam.cs.mpgc.rpg130579;

public class Player {
    private final String name;
    private final Item[] inventary;
    private String location;
    private int count;

    public Player(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Il nome non può essere vuoto");
        this.name = name;
        this.location = location;
        this.inventary = new Item[20];
        this.count = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCount() {
        return this.count;
    }

    public boolean addItem(Item i) {
        if (i == null) throw new IllegalArgumentException("L'oggetto non può essere null");
        if (count >= inventary.length) return false;
        if (hasItem(i.getId())) return false;
        inventary[count++] = i;
        return true;
    }

    public boolean hasItem(String itemId) {
        if (itemId == null || itemId.isBlank()) throw new IllegalArgumentException("L'ID dell'oggetto non deve essere vuoto");
        for (int i = 0; i < count; i++) {
            if (inventary[i].getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    public Item getItemAt(int index) {
        if (index < 0 || index >= count) throw new IllegalArgumentException("Indice non valido" + index);
        return inventary[index];
    }
}

