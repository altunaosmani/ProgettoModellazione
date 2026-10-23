package it.unicam.cs.mpgc.rpg130579.Model.Entity;

public class DocumentItem extends Item {
    private final String text;

    public DocumentItem(String id, String name, String description, String text) {
        super(id, name, description);
        if (text == null || text.isBlank()) throw new IllegalArgumentException("Il testo del documento non può essere vuoto");
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public boolean use(Object target) {
        return true;
    }
}
