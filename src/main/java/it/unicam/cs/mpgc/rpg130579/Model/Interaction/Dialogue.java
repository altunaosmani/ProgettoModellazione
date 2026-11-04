package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Dialogue {
    private final String title;
    private final List<String> lines;

    public Dialogue(String title, List<String> lines) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Il titolo del dialogo non può essere vuoto");
        if (lines == null || lines.isEmpty())
            throw new IllegalArgumentException("Il dialogo deve contenere almeno una battuta");
        for (String line : lines) {
            if (line == null || line.isBlank())
                throw new IllegalArgumentException("Le battute non possono essere vuote");
        }
        this.title = title;
        this.lines = Collections.unmodifiableList(new ArrayList<>(lines));
    }

    public String getTitle() {
        return this.title;
    }

    public int getLinesSize() {
        return this.lines.size();
    }

    public String getLine(int index) {
        if (index < 0 || index >= lines.size()) throw new IllegalArgumentException("indice della battuta non valido");
        return lines.get(index);
    }
}
