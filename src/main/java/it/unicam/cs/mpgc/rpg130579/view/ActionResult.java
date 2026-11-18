package it.unicam.cs.mpgc.rpg130579.view;

/** Esito di un'azione di gioco con flag di successo e messaggio per l'utente. */
public record ActionResult(boolean success, String message) {
    public static ActionResult ok() { return new ActionResult(true, ""); }
    public static ActionResult ok(String message) { return new ActionResult(true, message); }
    public static ActionResult fail(String message) { return new ActionResult(false, message); }
}
