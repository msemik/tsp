package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class TspTest {
    List<TspSolver> tspSolvers;

    private void solveWithEveryAlgorithm(String graphName, SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        for (TspSolver tspSolver : tspSolvers) {
            long beginTime = System.nanoTime();
            Collection<DefaultWeightedEdge> result = tspSolver.solve(graph);
            String solverName = tspSolver.getClass().getName();
            System.out.println("Result for graph '" + graphName + "' by '" + solverName + "' with cost " + countCost(graph, result));
            printGraph(graph);
            System.out.println("Execution time: " + (System.nanoTime() - beginTime) / 1000.0 / 1000.0 + " seconds\n-------");
        }
    }

    private double countCost(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Collection<DefaultWeightedEdge> edges) {
        Optional<Double> reduce = edges.stream().map(graph::getEdgeWeight).reduce((x, y) -> x + y);
        return reduce.isPresent() ? reduce.get() : -1;
    }


    private void printGraph(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        System.out.print("(" + graph.vertexSet() + ", [");
        for (Iterator<DefaultWeightedEdge> iterator = graph.edgeSet().iterator(); iterator.hasNext(); ) {
            DefaultWeightedEdge edge = iterator.next();
            System.out.print(edge + " " + graph.getEdgeWeight(edge));
            if (iterator.hasNext())
                System.out.print(", ");
        }
        System.out.println("])");

    }

    @Before
    public void setTspSolvers() {
        tspSolvers = new ArrayList<>();
        tspSolvers.add(new NearestNeighbourTspSolver());
        //fake solvers

        tspSolvers.add(new TspSolver() {
            @Override
            public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
                return graph.edgeSet();
            }
        });
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void triangle() throws Exception {

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new GraphBuilder()
                .addEdge("A", "B", 12)
                .addEdge("B", "C", 10)
                .addEdge("C", "A", 2)
                .build();

        solveWithEveryAlgorithm("triangle", graph);
    }

    @Test
    public void quad() throws Exception {

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new GraphBuilder()
                .addEdge("A", "B", 10)
                .addEdge("B", "C", 10)
                .addEdge("C", "D", 10)
                .addEdge("D", "A", 10)
                .addEdge("A", "C", 2)
                .addEdge("B", "D", 10)
                .build();

        solveWithEveryAlgorithm("triangle", graph);
    }

}
