package it.unicam.cs.mpgc.rpg130579.Model.World;

public class Exit {
    private final String id;
    private final String label;
    private final String destinationLocationId;
    private final Lockable barrier;

    public Exit(String id, String label, String destinationLocationId) {
        this(id, label, destinationLocationId, Lockable.NEVER_LOCKED);
    }

    public Exit(String id, String label, String destinationLocationId, Lockable barrier) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("L'uscita non può avere ID vuoto");
        if (label == null || label.isBlank()) throw new IllegalArgumentException("L'uscita non può avere un'etichetta vuota");
        if (destinationLocationId == null || destinationLocationId.isBlank()) throw new IllegalArgumentException("La destinazione non può essere vuota");
        if (barrier == null) throw new IllegalArgumentException("La barriera non può essere nulla");
        this.id = id;
        this.label = label;
        this.destinationLocationId = destinationLocationId;
        this.barrier = barrier;
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getDestinationLocationId() {
        return this.destinationLocationId;
    }

    public boolean isPassble() {
        return barrier.isUnlocked();
    }
}
