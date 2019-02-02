package pl.wturnieju.graph;

import org.springframework.lang.NonNull;

import com.google.common.collect.ComparisonChain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Edge<T> implements Comparable<Edge<T>> {

    private final int id;

    private Vertex<T> first;

    private Vertex<T> second;

    private Double weight;

    @Override
    public int compareTo(@NonNull Edge<T> o) {
        return ComparisonChain.start()
                .compare(weight, o.weight)
                .result();
    }

    public boolean isConnectingVertices(Vertex<T> first, Vertex<T> second) {
        return first.equals(this.first) && second.equals(this.second)
                || first.equals(this.second) && second.equals(this.first);
    }

    public boolean containsVertex(Vertex<T> v) {
        return first.equals(v) || second.equals(v);
    }

}
