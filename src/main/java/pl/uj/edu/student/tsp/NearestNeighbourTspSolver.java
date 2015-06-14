package pl.uj.edu.student.tsp;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class NearestNeighbourTspSolver implements TspSolver {
    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        List<DefaultWeightedEdge> l = new ArrayList<>();
        Set<String> visitedVertices = new HashSet<>();

        String currentVertex = graph.vertexSet().iterator().next();
        String firstVertex = currentVertex;
        while (true) {
            Set<DefaultWeightedEdge> edges;
            edges = graph.edgesOf(currentVertex);
            edges = removeEdgesContainingVertices(graph, edges, visitedVertices);

            if (edges.isEmpty()) {
                l.add(graph.getEdge(currentVertex, firstVertex));
                return l;
            }

            DefaultWeightedEdge edge = minimumWeight(edges, graph);
            l.add(edge);
            visitedVertices.add(currentVertex);
            currentVertex = Graphs.getOppositeVertex(graph, edge, currentVertex);
        }
    }

    private Set<DefaultWeightedEdge> removeEdgesContainingVertices(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Set<DefaultWeightedEdge> edges, Set<String> visitedVertexes) {
        return edges
                .stream()
                .filter(edge -> !visitedVertexes.contains(graph.getEdgeSource(edge)) && !visitedVertexes.contains(graph.getEdgeTarget(edge)))
                .collect(Collectors.toSet());
    }

    private DefaultWeightedEdge minimumWeight(Set<DefaultWeightedEdge> edges, SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        return edges
                .stream()
                .reduce((e1, e2) -> graph.getEdgeWeight(e1) <= graph.getEdgeWeight(e2) ? e1 : e2)
                .orElse(null);
    }
}
