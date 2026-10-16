package it.unicam.cs.mpgc.rpg130579;

public class Item extends ItemGame {
    private final String setFlag;

    public Item(String id, String name, String description) {
        this(id, name, description, null);
    }

    public Item(String id, String name, String description, String setFlag) {
        super(id, name, description);
        this.setFlag = setFlag;
    }

    public java.util.Optional<String> setsFlag() {
        return java.util.Optional.ofNullable(setFlag);
    }
}
