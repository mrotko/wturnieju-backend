package pl.wturnieju.graph;

import java.util.HashMap;
import java.util.Map;

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

    public boolean hasEdge(V vertex) {
        return edges.containsKey(vertex);
    }

    public abstract V getEdge();
}
