package pl.wturnieju.graph;

public class GraphFactory<T> {

    private int vertexId = 0;

    private int edgeId = 0;

    public void reset() {
        vertexId = 0;
        edgeId = 0;
    }

    private int nextEdgeId() {
        return edgeId++;
    }

    private int nextVertexId() {
        return vertexId++;
    }

    public Edge<T> createEdge(Vertex<T> first, Vertex<T> second) {
        var edge = new Edge<T>(nextEdgeId());

        edge.setFirst(first);
        edge.setSecond(second);
        edge.setWeight(0.);

        return edge;
    }

    public Vertex<T> createVertex(T value) {
        return new Vertex<>(nextVertexId(), value);
    }

}
