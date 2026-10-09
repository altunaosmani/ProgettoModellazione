package it.unicam.cs.mpgc.rpg130579;

public class Oggetto {
    private final String id;
    private final String nome;
    private final String descrizione;

    public Oggetto(String id, String nome, String descrizione) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'id non può essere vuoto");
        if (nome == null || nome.isBlank()) throw new IllegalArgumentException("Il nome non può essere vuoto");
        if (descrizione == null || descrizione.isBlank()) throw new IllegalArgumentException("La descrizione non può essere vuota");
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
