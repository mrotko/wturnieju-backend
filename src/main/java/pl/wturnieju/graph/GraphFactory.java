package pl.wturnieju.graph;

import java.util.concurrent.atomic.AtomicInteger;

public class GraphFactory<T> {

    private AtomicInteger vertexIdGenerator = new AtomicInteger(0);

    private AtomicInteger edgeIdGenerator = new AtomicInteger(0);

    public void reset() {
        vertexIdGenerator.set(0);
        edgeIdGenerator.set(0);
    }

    public Edge<T> createEdge(Vertex<T> first, Vertex<T> second) {
        var edge = new Edge<T>(edgeIdGenerator.getAndIncrement());

        edge.setFirst(first);
        edge.setSecond(second);
        edge.setWeight(0.);

        return edge;
    }

    public Vertex<T> createVertex(T value) {
        return new Vertex<>(vertexIdGenerator.getAndIncrement(), value);
    }

}
