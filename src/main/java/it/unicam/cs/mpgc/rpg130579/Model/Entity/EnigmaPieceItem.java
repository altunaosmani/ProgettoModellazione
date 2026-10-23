package it.unicam.cs.mpgc.rpg130579.Model.Entity;

public class EnigmaPieceItem extends Item {
    private final String phraseFragment;
    private final int orderPosition;

    public EnigmaPieceItem(String id, String name, String description, String phraseFragment, int orderPosition) {
        super (id, name, description);
        if (phraseFragment == null || phraseFragment.isBlank()) throw new IllegalArgumentException("Il frammento di frase non può essere vuoto");
        if (orderPosition < 0) throw new IllegalArgumentException("Non può essere accettato un numero negativo");
        this.phraseFragment = phraseFragment;
        this.orderPosition = orderPosition;
    }

    public String getPhraseFragment() {
        return this.phraseFragment;
    }

    public int getOrderPosition() {
        return this.orderPosition;
    }

    @Override
    public boolean use(Object target) {
        return false;
    }
}
