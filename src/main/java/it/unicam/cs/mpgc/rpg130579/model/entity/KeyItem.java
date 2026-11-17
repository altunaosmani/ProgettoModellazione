package it.unicam.cs.mpgc.rpg130579.model.entity;

public class KeyItem extends Item implements MechanismKey {
    private final String targetMechanismId;

    public KeyItem(String id, String name, String description, String targetMechanismId) {
        super(id, name, description);
        if (targetMechanismId == null || targetMechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo bersaglio non può essere vuoto");
        this.targetMechanismId = targetMechanismId;
    }

    public String getTargetMechanismId() {
        return targetMechanismId;
    }

    @Override
    public boolean opens(String mechanismId) {
        return mechanismId != null && targetMechanismId.equalsIgnoreCase(mechanismId);
    }
}