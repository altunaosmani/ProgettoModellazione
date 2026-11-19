package it.unicam.cs.mpgc.rpg130579.model.entity;

/**
 * Oggetto leggibile (libro, lettera, appunto) che espone un contenuto testuale aggiuntivo.
 */
public class DocumentItem extends Item {
    private final String text;
    private final boolean portable;

    public DocumentItem(String id, String name, String description, String text) {
        this(id, name, description, text, true);
    }

    public DocumentItem(String id, String name, String description, String text, boolean portable) {
        super(id, name, description);
        if (text == null || text.isBlank())
            throw new IllegalArgumentException("Il testo del documento non può essere vuoto");
        this.text = text;
        this.portable = portable;
    }

    /** Documento che si può leggere sul posto ma non raccogliere. */
    public static DocumentItem readOnly(String id, String name, String description, String text) {
        return new DocumentItem(id, name, description, text, false);
    }

    public String getText() {
        return this.text;
    }

    @Override
    public boolean isPortable() {
        return portable;
    }

    @Override
    public String examine() {
        return getDescription() + "\n" + text;
    }
}
