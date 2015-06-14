package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Ignore;
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
            System.out.println("Execution time: " + (System.nanoTime() - beginTime) / 1e9 + " seconds\n-------");
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
        tspSolvers.add(new ChristofidesTspSolver());
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

        solveWithEveryAlgorithm("quad", graph);
    }

    @Test
    public void christofidesWikipediaExample() throws Exception {

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new GraphBuilder()
                .addEdge("A", "B", 1)
                .addEdge("A", "D", 1)
                .addEdge("A", "C", 1)
                .addEdge("A", "E", 2)
                .addEdge("B", "C", 1)
                .addEdge("B", "E", 1)
                .addEdge("B", "D", 2)
                .addEdge("E", "C", 1)
                .addEdge("E", "D", 1)
                .addEdge("D", "C", 1)
                .build();

        solveWithEveryAlgorithm("christofidesWikipediaExample", graph);
    }

    @Test
    public void christofidesWikipediaExample2() throws Exception {

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new GraphBuilder()
                .addEdge("A", "B", 5)
                .addEdge("B", "C", 5)

                .addEdge("D", "B", 5)
                .addEdge("B", "E", 5)

                .addEdge("A", "C", 10)  //zamiana tego z poniższym daje inne wyniki
                .addEdge("D", "E", 6)

                .addEdge("A", "D", 10)
                .addEdge("A", "E", 10)
                .addEdge("E", "C", 10)
                .addEdge("D", "C", 10)
                .build();

        solveWithEveryAlgorithm("christofidesWikipediaExample2", graph);
    }
}
