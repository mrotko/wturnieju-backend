package pl.wturnieju.graph;

public class SimpleVertex<T> extends Vertex<T, SimpleVertex<T>> {

    public SimpleVertex(int id, T value) {
        super(id, value);
    }

    public void addEdge(SimpleVertex<T> vertex) {
        edges.put(vertex, 0.);
        vertex.edges.put(this, 0.);
    }

    public void removeEdge(SimpleVertex<T> vertex) {
        edges.remove(vertex);
        vertex.edges.remove(this);
    }
}
