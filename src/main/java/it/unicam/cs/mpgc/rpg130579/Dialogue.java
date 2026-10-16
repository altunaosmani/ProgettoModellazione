package it.unicam.cs.mpgc.rpg130579;

import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    private final String title;
    private final List<String> lines;
    private int indexLines;

    public Dialogue(String title, List<String> lines) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Il titolo non può essere vuoto");
        if (lines == null || lines.isEmpty())
            throw new IllegalArgumentException("Il dialogo deve contenere almeno una battuta");
        for (String line : lines) {
            if (line == null || line.isBlank())
                throw new IllegalArgumentException("Le battute non devono essere vuote");
        }
        this.title = title;
        this.lines = new ArrayList<>(lines);
        this.indexLines = 0;
    }

    public String getTitolo() {
        return this.title;
    }

    public int getNumeroBattue() {
        return this.lines.size();
    }

    public boolean battutaDopo() {
        return indexLines < lines.size();
    }

    public String battutaSuccessiva() {
        if (!battutaDopo()) throw new IllegalStateException("Il dialogo è terminato");
        String battuta = lines.get(indexLines++);
        return battuta;
    }

    public void reset() {
        indexLines = 0;
    }
}
