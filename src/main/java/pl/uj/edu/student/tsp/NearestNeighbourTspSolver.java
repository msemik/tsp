package pl.uj.edu.student.tsp;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by michal on 13.06.15.
 */
public class NearestNeighbourTspSolver implements TspSolver {
    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        List<DefaultWeightedEdge> l = new ArrayList<>();
        Set<String> visitedVertexes = new HashSet<>();

        String currentVertex = graph.vertexSet().iterator().next();
        while (true) {
            Set<DefaultWeightedEdge> edges;
            edges = graph.edgesOf(currentVertex);
            edges = removeEdgesContainingVertexes(graph, edges, visitedVertexes);

            if (edges.isEmpty())
                return l;

            DefaultWeightedEdge edge = minimumWeight(edges, graph);
            l.add(edge);
            visitedVertexes.add(currentVertex);
            currentVertex = Graphs.getOppositeVertex(graph, edge, currentVertex);
        }
    }

    private Set<DefaultWeightedEdge> removeEdgesContainingVertexes(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Set<DefaultWeightedEdge> edges, Set<String> visitedVertexes) {
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
