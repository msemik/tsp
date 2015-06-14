package pl.uj.edu.student.tsp;

import org.jgrapht.alg.EulerianCircuit;
import org.jgrapht.alg.PrimMinimumSpanningTree;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.jgrapht.graph.builder.UndirectedWeightedGraphBuilder;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alanhawrot
 */
public class ChristofidesTspSolver implements TspSolver {

    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> mst = createMinimumSpanningTree(graph);
        Set<String> oddDegreeVertices = findOddDegreeVertices(mst);
        SimpleWeightedGraph<String, DefaultWeightedEdge> completeGraphOverOddDegreeVertices = createCompleteGraphWithSetEdgeWeights(oddDegreeVertices, graph);
        PerfectMatchingFinder perfectMatchingFinder = new MinimumSumPerfectMatchingAdapter();
        SimpleWeightedGraph<String, DefaultWeightedEdge> perfectMatchingGraph = perfectMatchingFinder.findPerfectMatching(completeGraphOverOddDegreeVertices);
        WeightedMultigraph<String, DefaultWeightedEdge> multigraph = createMultigraph(mst, perfectMatchingGraph);
        List<String> eulerianCircuit = findEulerianCircuit(multigraph);
        SimpleWeightedGraph<String, DefaultWeightedEdge> hamiltonianGraph = makeHamiltonianGraph(eulerianCircuit, graph);

        return hamiltonianGraph.edgeSet();
    }

    private SimpleWeightedGraph<String, DefaultWeightedEdge> createMinimumSpanningTree(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        PrimMinimumSpanningTree<String, DefaultWeightedEdge> primMst = new PrimMinimumSpanningTree<>(graph);
        Set<DefaultWeightedEdge> primMstEdgeSet = primMst.getMinimumSpanningTreeEdgeSet();

        UndirectedWeightedGraphBuilder<String, DefaultWeightedEdge, SimpleWeightedGraph<String, DefaultWeightedEdge>> undirectedWeightedGraphBuilder
                = new UndirectedWeightedGraphBuilder<>(new SimpleWeightedGraph<>(DefaultWeightedEdge.class));

        primMstEdgeSet.forEach(edge -> undirectedWeightedGraphBuilder.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge), graph.getEdgeWeight(edge)));

        return undirectedWeightedGraphBuilder.build();
    }

    private Set<String> findOddDegreeVertices(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        Set<String> oddDegreeVertices = new HashSet<>();

        graph.vertexSet().forEach(vertex -> {
            if (graph.degreeOf(vertex) % 2 != 0) {
                oddDegreeVertices.add(vertex);
            }
        });

        return oddDegreeVertices;
    }

    private SimpleWeightedGraph<String, DefaultWeightedEdge> createCompleteGraphWithSetEdgeWeights(Set<String> vertices,
                                                                                                   SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        SimpleWeightedGraph<String, DefaultWeightedEdge> completeGraph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (String source : vertices) {
            vertices.stream().filter(target -> !source.equals(target) && !completeGraph.containsEdge(source, target)).forEach(target -> {
                DefaultWeightedEdge edge = graph.getEdge(source, target);
                Double weight = graph.getEdgeWeight(edge);
                if (!completeGraph.vertexSet().contains(source)) {
                    completeGraph.addVertex(source);
                }
                if (!completeGraph.vertexSet().contains(target)) {
                    completeGraph.addVertex(target);
                }
                DefaultWeightedEdge addedEdge = completeGraph.addEdge(source, target);
                completeGraph.setEdgeWeight(addedEdge, weight);
            });
        }

        return completeGraph;
    }

    private WeightedMultigraph<String, DefaultWeightedEdge> createMultigraph(SimpleWeightedGraph<String, DefaultWeightedEdge> mst,
                                                                             SimpleWeightedGraph<String, DefaultWeightedEdge> perfectMatchingGraph) {
        WeightedMultigraph<String, DefaultWeightedEdge> multigraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        mst.edgeSet().forEach(edge -> {
            String source = mst.getEdgeSource(edge);
            String target = mst.getEdgeTarget(edge);
            if (!multigraph.vertexSet().contains(source)) {
                multigraph.addVertex(source);
            }
            if (!multigraph.vertexSet().contains(target)) {
                multigraph.addVertex(target);
            }
            DefaultWeightedEdge addedEdge = multigraph.addEdge(source, target);
            multigraph.setEdgeWeight(addedEdge, mst.getEdgeWeight(edge));
        });

        perfectMatchingGraph.edgeSet().forEach(edge -> {
            String source = perfectMatchingGraph.getEdgeSource(edge);
            String target = perfectMatchingGraph.getEdgeTarget(edge);
            if (!multigraph.vertexSet().contains(source)) {
                multigraph.addVertex(source);
            }
            if (!multigraph.vertexSet().contains(target)) {
                multigraph.addVertex(target);
            }
            DefaultWeightedEdge addedEdge = multigraph.addEdge(source, target);
            multigraph.setEdgeWeight(addedEdge, perfectMatchingGraph.getEdgeWeight(edge));
        });

        return multigraph;
    }

    private List<String> findEulerianCircuit(WeightedMultigraph<String, DefaultWeightedEdge> multigraph) {
        return EulerianCircuit.getEulerianCircuitVertices(multigraph);
    }

    private SimpleWeightedGraph<String, DefaultWeightedEdge> makeHamiltonianGraph(List<String> eulerianCircuit,
                                                                                  SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        UndirectedWeightedGraphBuilder<String, DefaultWeightedEdge, SimpleWeightedGraph<String, DefaultWeightedEdge>> undirectedWeightedGraphBuilder
                = new UndirectedWeightedGraphBuilder<>(new SimpleWeightedGraph<>(DefaultWeightedEdge.class));

        for (int i = 1; i < eulerianCircuit.size(); i++) {
            String source = eulerianCircuit.get(i - 1);
            String target = eulerianCircuit.get(i);

            if (!isVertexVisited(eulerianCircuit, target, i)) {
                undirectedWeightedGraphBuilder.addEdge(source, target, graph.getEdgeWeight(graph.getEdge(source, target)));
            } else {
                int nextUnvisitedVertexIndex = getNextUnvisitedVertexIndex(eulerianCircuit, i + 1);
                if (nextUnvisitedVertexIndex != -1) {
                    String newTarget = eulerianCircuit.get(nextUnvisitedVertexIndex);
                    undirectedWeightedGraphBuilder.addEdge(source, newTarget, graph.getEdgeWeight(graph.getEdge(source, newTarget)));
                    i = nextUnvisitedVertexIndex + 1;
                } else {
                    String newTarget = eulerianCircuit.get(eulerianCircuit.size() - 1);
                    undirectedWeightedGraphBuilder.addEdge(source, newTarget, graph.getEdgeWeight(graph.getEdge(source, newTarget)));
                    break;
                }
            }
        }

        return undirectedWeightedGraphBuilder.build();
    }

    private boolean isVertexVisited(List<String> eulerianCircuit, String vertex, int index) {
        for (int i = 0; i < index; i++) {
            if (eulerianCircuit.get(i).equals(vertex)) {
                return true;
            }
        }
        return false;
    }

    private int getNextUnvisitedVertexIndex(List<String> eulerianCircuit, int index) {
        for (int i = index; i < eulerianCircuit.size(); i++) {
            if (!isVertexVisited(eulerianCircuit, eulerianCircuit.get(i), i)) {
                return i;
            }
        }
        return -1;
    }
}
