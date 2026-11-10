package it.unicam.cs.mpgc.rpg130579.Model.Interaction;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dialogue {
    private static final String LINEAR_NODE_ID = "start";

    private final String title;
    private final String startNodeId;
    private final Map<String, DialogueNode> nodes;

    public Dialogue(String title, String startNodeId, Collection<DialogueNode> nodes) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Il titolo del dialogo non può essere vuoto");
        if (startNodeId == null || startNodeId.isBlank()) throw new IllegalArgumentException("L'ID del nodo iniziale non può essere vuoto");
        if (nodes == null || nodes.isEmpty()) throw new IllegalArgumentException("Il dialogo deve avere almeno un nodo");

        Map<String, DialogueNode> map = new LinkedHashMap<>();
        for (DialogueNode node : nodes) {
            if (node == null) throw new IllegalArgumentException("Il nodo non può essere nullo");
            if (map.putIfAbsent(node.getId(), node) != null)
                throw new IllegalArgumentException("Esistono due nodi con lo stesso id: " + node.getId());
        }
        if (!map.containsKey(startNodeId))
            throw new IllegalArgumentException("Il nodo iniziale non esiste: " + startNodeId);
        for (DialogueNode node : map.values()) {
            for (DialogueChoice choice : node.getChoices()) {
                if (!choice.endsDialogue() && !map.containsKey(choice.nextNodeId()))
                    throw new IllegalArgumentException("La scelta \"" + choice.text() + "\" porta a un nodo inesistente: " + choice.nextNodeId());
            }
        }
        this.title = title;
        this.startNodeId = startNodeId;
        this.nodes = Collections.unmodifiableMap(map);
    }

    /** Dialogo lineare: solo battute, nessuna scelta. */
    public Dialogue(String title, List<String> lines) {
        this(title, LINEAR_NODE_ID, List.of(new DialogueNode(LINEAR_NODE_ID, lines)));
    }

    public String getTitle() {
        return title;
    }

    public DialogueNode getStartNode() {
        return nodes.get(startNodeId);
    }

    public DialogueNode getNode(String id) {
        DialogueNode node = nodes.get(id);
        if (node == null) throw new IllegalArgumentException("Nodo inesistente: " + id);
        return node;
    }
}
