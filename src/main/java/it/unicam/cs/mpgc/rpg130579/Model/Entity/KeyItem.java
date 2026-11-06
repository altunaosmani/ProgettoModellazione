package it.unicam.cs.mpgc.rpg130579.Model.Entity;

public class KeyItem extends Item {
    private final String targetMechanismId;

    public KeyItem(String id, String name, String descripion, String targetMechanismId) {
        super(id, name, descripion);
        if (targetMechanismId == null || targetMechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo bersaglio non può essere vuoto");
        this.targetMechanismId = targetMechanismId;
    }

    public String getTargetMechanismId() {
        return targetMechanismId;
    }

    public boolean opens(String mechanismId) {
        return mechanismId != null && targetMechanismId.equalsIgnoreCase(mechanismId);
    }
}
