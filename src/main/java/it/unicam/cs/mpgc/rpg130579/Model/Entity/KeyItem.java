package it.unicam.cs.mpgc.rpg130579.Model.Entity;

public class KeyItem extends Item {
    private final String targetMechanismId;

    public KeyItem(String id, String name, String descriprion,String targetMechanismId) {
        super(id, name, descriprion);
        if (targetMechanismId == null || targetMechanismId.isBlank()) throw new IllegalArgumentException("L'ID del meccanismo bersaglio non può essere vuoto");
        this.targetMechanismId = targetMechanismId;
    }

    public String getTargetMechanismId() {
        return targetMechanismId;
    }

    @Override
    public boolean use(Object target) {
        if (target instanceof String && ((String) target).equalsIgnoreCase(targetMechanismId)) return true;
        return false;
    }
}
