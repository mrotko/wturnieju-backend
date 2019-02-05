package pl.wturnieju.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

public abstract class Graph<T> {

    private final BiFunction<T, T, Double> weightCalculationMethod;

    protected final GraphFactory<T> graphFactory;

    protected List<Vertex<T>> vertices = new ArrayList<>();

    protected List<Edge<T>> edges = new ArrayList<>();

    private Deque<Vertex<T>> shortestPath = new ArrayDeque<>();

    public Graph(BiFunction<T, T, Double> weightCalculationMethod, GraphFactory<T> graphFactory) {
        this.weightCalculationMethod = weightCalculationMethod;
        this.graphFactory = graphFactory;
    }

    public abstract void generateGraph(List<T> verticesValues);

    protected void resetGraph() {
        graphFactory.reset();
        vertices.clear();
        shortestPath.clear();
    }

    private void addEdgesToVertices() {
        vertices.forEach(v -> getEdgesWithVertex(v).forEach(v::addEdge));
    }

    public void makeFinalSetup() {
        addEdgesToVertices();
        recalculateEdgesWeights();
    }

    private List<Edge<T>> getEdgesWithVertex(Vertex<T> vertex) {
        return edges.stream()
                .filter(e -> e.containsVertex(vertex))
                .collect(Collectors.toList());
    }

    public List<Edge<T>> getEdges() {
        return edges;
    }

    private void recalculateEdgesWeights() {
        edges.forEach(edge -> edge.setWeight(calculateEdgeWeight(edge)));
    }

    private Double calculateEdgeWeight(Edge<T> edge) {
        return weightCalculationMethod.apply(edge.getFirst().getValue(), edge.getSecond().getValue());
    }

    public void unlinkVertexesWithValues(List<ImmutablePair<T, T>> pairs) {
        pairs.stream()
                .map(this::mapValuesPairToVerticesPair)
                .filter(vPair -> vPair.getLeft() != null && vPair.getRight() != null)
                .forEach(verticesPair -> removeEdgeWithVertices(verticesPair.getLeft(), verticesPair.getRight()));
    }

    private ImmutablePair<Vertex<T>, Vertex<T>> mapValuesPairToVerticesPair(ImmutablePair<T, T> valuesPair) {
        return ImmutablePair.of(getVertexByValue(valuesPair.getLeft()), getVertexByValue(valuesPair.getRight()));
    }

    private void removeEdgeWithVertices(Vertex<T> first, Vertex<T> second) {
        edges.removeIf(edge -> edge.isConnectingVertices(first, second));
    }

    private Vertex<T> getVertexByValue(T value) {
        return vertices.stream()
                .filter(v -> Objects.equals(value, v.getValue()))
                .findFirst().orElse(null);
    }

    public void findPath() {
        shortestPath.clear();
        if (vertices.isEmpty()) {
            return;
        }

        shortestPath.add(vertices.get(0));
        shortestPath.getLast().setUsed(true);

        while (!shortestPath.isEmpty() && (shortestPath.size() != vertices.size())) {
            var lastVertex = shortestPath.getLast();
            var lightestEdge = lastVertex.getLightestAvailableEdge();
            if (lightestEdge == null) {
                shortestPath.removeLast();
                lastVertex.resetVisitedEdges();
                lastVertex.setUsed(false);
            } else {
                var nextVertex = lastVertex.getOtherVertexFromEdge(lightestEdge);
                shortestPath.addLast(nextVertex);
                nextVertex.setUsed(true);
                lastVertex.markEdgeAsVisited(lightestEdge);
            }
        }
    }

    public Deque<Vertex<T>> getPath() {
        return shortestPath;
    }
}
