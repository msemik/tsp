package pl.uj.edu.student.tsp;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class TspTest {
    List<TspSolver> tspSolvers;

    private void solveWithEveryAlgorithm(String graphName, SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        for (TspSolver tspSolver : tspSolvers) {
            long beginTime = System.nanoTime();
            Collection<DefaultWeightedEdge> result = tspSolver.solve(graph);
            String solverName = tspSolver.getClass().getName();
            System.out.println("Result for graph '" + graphName + "' by '" + solverName + "' with cost " + countCost(graph, result));
            printGraph(graph);
            assertNoLoops(graph, result);
            assertCorrectEdgesAmount(graph, result);// correct edges amount mean correct vertices amount.
            assertUniqueVertices(graph, result);
            System.out.println("Execution time: " + (System.nanoTime() - beginTime) / 1e9 + " seconds\n-------");
        }
    }

    private void assertNoLoops(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Collection<DefaultWeightedEdge> result) {
        for (DefaultWeightedEdge e : result) {
            assertThat(format("%s is invalid edge. Loops are not allowed", e), distinctVerticesInEdge(graph, e), is(true));
        }
    }

    private boolean distinctVerticesInEdge(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, DefaultWeightedEdge e) {
        return !graph.getEdgeSource(e).equals(graph.getEdgeTarget(e));
    }

    private void assertCorrectEdgesAmount(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Collection<DefaultWeightedEdge> result) {
        int verticesNumber = graph.vertexSet().size();
        int verticesInTour = result.size();
        assertThat(
                format("edges number in tour (%d) must be equal to total vertices in graph (%d)", verticesInTour, verticesNumber),
                //In other words, vertices number in graph must be equal to vertices number in tour.
                verticesNumber, is(verticesInTour));
    }

    private void assertUniqueVertices(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Collection<DefaultWeightedEdge> result) {
        List<String> visitedVertices = new ArrayList<>();
        DefaultWeightedEdge lastEdge = null;
        String begin = getBeginVertex(graph, result);
        for (DefaultWeightedEdge edge : result) {
            if (lastEdge == null) {
                //wykona się przy pierwszej krawędzi
                //ważne jest aby dodać wierzchołki odwiedzone w odpowiedniej kolejności.
                lastEdge = edge;
                visitedVertices.add(begin);
                visitedVertices.add(Graphs.getOppositeVertex(graph, edge, begin));
            } else {
                String lastVertex = visitedVertices.get(visitedVertices.size() - 1);
                assertThat(
                        format("%s - %s is not a valid path fragment", lastEdge, edge),
                        vertexBelongToEdge(graph, edge, lastVertex), is(true));

                String v = Graphs.getOppositeVertex(graph, edge, lastVertex);
                assertThat(format("%s may not be visited twice in tour", v), result, not(contains(v)));
                visitedVertices.add(v);
                lastEdge = edge;
            }
        }
        assertThat("not a valid tour (last edge is not connected with first vertex)", vertexBelongToEdge(graph, lastEdge, begin), is(true));
    }

    private String getBeginVertex(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Collection<DefaultWeightedEdge> result) {
        Iterator<DefaultWeightedEdge> iterator = result.iterator();
        DefaultWeightedEdge firstEdge = iterator.next();
        DefaultWeightedEdge secondEdge = iterator.next();

        String v1 = graph.getEdgeSource(firstEdge);
        String v2 = graph.getEdgeTarget(firstEdge);

        if (!vertexBelongToEdge(graph, secondEdge, v1))
            return v1;
        if (!vertexBelongToEdge(graph, secondEdge, v2))
            return v2;
        throw new IllegalStateException(format("Not a valid path fragment %s - %s", firstEdge, secondEdge));
    }

    private boolean vertexBelongToEdge(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, DefaultWeightedEdge edge, String v) {
        return graph.getEdgeSource(edge).equals(v) || graph.getEdgeTarget(edge).equals(v);
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


    @Test
    public void exampleFromPhoto() throws Exception {

        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = new GraphBuilder()
                //krawędzie w cyklu.
                .addEdge("A", "B", 5)
                .addEdge("B", "C", 5)
                .addEdge("C", "E", 5)
                .addEdge("B", "E", 5)
                .addEdge("B", "D", 5)
                .addEdge("D", "A", 5)

                        //możliwe skoki
                .addEdge("A", "E", 10) //zmiana tych dwóch zmienia wyniki
                .addEdge("D", "C", 6)


                .addEdge("A", "C", 10)
                .addEdge("D", "E", 10)
                .build();

        solveWithEveryAlgorithm("exampleFromPhoto", graph);
    }
}
