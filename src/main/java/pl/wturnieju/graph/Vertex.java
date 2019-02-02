package pl.wturnieju.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Vertex<T> {

    private final int id;

    private final T value;

    private List<Edge<T>> edges = new ArrayList<>();

    private List<Edge<T>> visitedEdges = new ArrayList<>();

    private boolean used = false;

    public void resetVisitedEdges() {
        visitedEdges.clear();
    }

    public void addEdge(Edge<T> edge) {
        edges.add(edge);
    }

    public void markEdgeAsVisited(Edge<T> edge) {
        visitedEdges.add(edge);
    }

    public Edge<T> getLightestAvailableEdge() {
        return edges.stream()
                .filter(e -> !getOtherVertexFromEdge(e).isUsed())
                .filter(e -> !isEdgeVisited(e))
                .min(Comparator.comparing(Edge::getWeight))
                .orElse(null);
    }

    private boolean isEdgeVisited(Edge<T> e) {
        return visitedEdges.contains(e);
    }

    public Vertex<T> getOtherVertexFromEdge(Edge<T> edge) {
        if (!edge.getFirst().equals(this)) {
            return edge.getFirst();
        }
        return edge.getSecond();
    }
}
