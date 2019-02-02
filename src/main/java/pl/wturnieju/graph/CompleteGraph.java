package pl.wturnieju.graph;

import java.util.List;
import java.util.function.BiFunction;

public class CompleteGraph<T> extends Graph<T> {
    public CompleteGraph(BiFunction<T, T, Double> weightCalculationMethod, GraphFactory<T> graphFactory) {
        super(weightCalculationMethod, graphFactory);
    }

    @Override
    public void generateGraph(List<T> verticesValues) {
        resetGraph();
        generateVertices(verticesValues);
        generateEdges();
    }

    private void generateVertices(List<T> verticesValues) {
        verticesValues.forEach(value -> vertices.add(graphFactory.createVertex(value)));
    }

    private void generateEdges() {
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                edges.add(graphFactory.createEdge(vertices.get(i), vertices.get(j)));
            }
        }
    }
}
