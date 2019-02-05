package pl.wturnieju.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.BiFunction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompleteGraphTest {

    private GraphFactory<String> graphFactory;

    @BeforeEach
    void setUp() {
        graphFactory = new GraphFactory<>();
    }

    @Test
    public void shouldReturnEmptyPathWhenNoData() {
        Graph<String> graph = new CompleteGraph<>(Mockito.mock(BiFunction.class), graphFactory);
        graph.generateGraph(Collections.emptyList());
        graph.findPath();
        graph.makeFinalSetup();
        Assertions.assertEquals(0, graph.getPath().size());
    }

    @Test
    void shouldReturnSingleEdgeWhenTwoElementsInput() {
        Graph<String> graph = new CompleteGraph<>(Mockito.mock(BiFunction.class), graphFactory);
        graph.generateGraph(Arrays.asList("1", "2"));
        graph.makeFinalSetup();
        Assertions.assertEquals(1, graph.getEdges().size());
    }
}