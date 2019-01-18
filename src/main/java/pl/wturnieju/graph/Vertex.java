package pl.wturnieju.graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public abstract class Vertex<T, V extends Vertex<T, V>> {

    protected final int id;

    protected final T value;

    protected Map<V, Double> edges = new HashMap<>();

    protected Set<V> visitedEdges = new HashSet<>();

    public boolean hasEdge(V vertex) {
        return edges.containsKey(vertex);
    }

    public V getFirstEdge() {
        var it = edges.entrySet().iterator();
        if (it.hasNext()) {
            return it.next().getKey();
        }
        return null;
    }

    public V getEdgeAndVisit() {
        V v = getNotVisitedEdge();
        markAsVisited(v);
        return v;
    }

    public V getNotVisitedEdge() {
        for (var entry : edges.entrySet()) {
            var v = entry.getKey();
            if (!visitedEdges.contains(v)) {
                return v;
            }
        }
        return null;
    }

    public List<V> getNotVisitedEdges() {
        return edges.keySet().stream()
                .filter(e -> !visitedEdges.contains(e))
                .collect(Collectors.toList());
    }

    public void markAsVisited(V edge) {
        visitedEdges.add(edge);
    }
}
