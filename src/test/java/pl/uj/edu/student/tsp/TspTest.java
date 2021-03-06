package pl.uj.edu.student.tsp;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
            long beginTime = System.currentTimeMillis();
            Collection<DefaultWeightedEdge> result = tspSolver.solve(graph);
            String solverName = tspSolver.getClass().getName();
            System.out.println("Result for graph '" + graphName + "' by '" + solverName + "' with cost " + countCost(graph, result));
            printGraph(graph);
            assertNoLoops(graph, result);
            assertCorrectEdgesAmount(graph, result);// correct edges amount mean correct vertices amount.
            assertUniqueVertices(graph, result);
            double totalSeconds = (System.currentTimeMillis() - beginTime) / 1e3;
            System.out.println("Execution time: " + totalSeconds + " seconds\n-------");
            System.out.flush();
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
        if (graph.vertexSet().size() > 10) {
            System.out.println("(big graph)");
        } else {
            System.out.print("(" + graph.vertexSet() + ", [");
            for (Iterator<DefaultWeightedEdge> iterator = graph.edgeSet().iterator(); iterator.hasNext(); ) {
                DefaultWeightedEdge edge = iterator.next();
                System.out.print(edge + " " + graph.getEdgeWeight(edge));
                if (iterator.hasNext())
                    System.out.print(", ");
            }
            System.out.println("])");
        }
    }

    @Before
    public void setTspSolvers() {
        tspSolvers = new ArrayList<>();
        tspSolvers.add(new NearestNeighbourTspSolver());
        tspSolvers.add(new ChristofidesTspSolver());
        tspSolvers.add(new NearestInsertionTspSolver());
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

    @Test
    public void about30WesternSaharaCities() throws Exception {
        //http://www.math.uwaterloo.ca/tsp/world/countries.html#QA
        String filename = "src/test/java/pl/uj/edu/student/tsp/wi29.tsp";
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = getGraphFromTspLibFormatFile(filename);
        solveWithEveryAlgorithm("about30WesternSaharaCities, optimal tour: 27603", graph);
    }

    @Test
    public void about200QatarCities() throws Exception {
        String filename = "src/test/java/pl/uj/edu/student/tsp/qa194.tsp";
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = getGraphFromTspLibFormatFile(filename);
        solveWithEveryAlgorithm("about200QatarCities, optimal tour: 9352", graph);
    }

    @Test
    public void about700UruguayCities() throws Exception {
        String filename = "src/test/java/pl/uj/edu/student/tsp/uy734.tsp";
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = getGraphFromTspLibFormatFile(filename);
        solveWithEveryAlgorithm("about700UruguayCities, optimal tour: 79114", graph);
    }

    @Test
    public void about2000CitiesInOman() throws Exception {
        String filename = "src/test/java/pl/uj/edu/student/tsp/mu1979.tsp";
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = getGraphFromTspLibFormatFile(filename);
        solveWithEveryAlgorithm("about2000CitiesInOman, optimal tour: 86891", graph);
    }

    @Test
    @Ignore
    public void about10000FinlandCities() throws Exception {
        String filename = "src/test/java/pl/uj/edu/student/tsp/fi10639.tsp";
        SimpleWeightedGraph<String, DefaultWeightedEdge> graph = getGraphFromTspLibFormatFile(filename);
        solveWithEveryAlgorithm("about10000FinlandCities, known tour: 520,527", graph);
    }

    private SimpleWeightedGraph<String, DefaultWeightedEdge> getGraphFromTspLibFormatFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(filename));
        do {
            String s = scanner.nextLine();
            if (s.trim().equals("NODE_COORD_SECTION"))
                break;
        } while (true);

        CoordinateBasedGraphBuilder graphBuilder = new CoordinateBasedGraphBuilder();

        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            if (s.trim().equals("EOF"))
                break;
            String[] split = s.trim().split("\\s+");
            graphBuilder.addVertex(split[0], Double.valueOf(split[1]), Double.valueOf(split[2]));
        }
        return graphBuilder.build();
    }
}
