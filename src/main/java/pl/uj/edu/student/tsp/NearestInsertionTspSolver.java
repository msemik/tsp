package pl.uj.edu.student.tsp;

import com.google.common.collect.Sets;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class NearestInsertionTspSolver implements TspSolver {
    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        List<String> tour = new ArrayList<>();
        tour.addAll(find3Clique(graph));
        FloydWarshallShortestPaths shortestPaths = new FloydWarshallShortestPaths(graph);

        while (tour.size() < graph.edgeSet().size()) {
            DefaultWeightedEdge closest = minimumWeightEdgePointingOutsideTour(graph, tour);
            String vertexOutsideTour = vertexOutsideTourFromEdge(graph, tour, closest);
            String vertexInTour = Graphs.getOppositeVertex(graph, closest, vertexOutsideTour);
            HashSet<String> secondEdgeCandidates = new HashSet<>(tour);
            secondEdgeCandidates.remove(vertexInTour);
            GraphPath<String, DefaultWeightedEdge> shortestPath = null;
            for (String candidate : secondEdgeCandidates) {
                GraphPath path = shortestPaths.getShortestPath(vertexOutsideTour, candidate);
                if (shortestPath == null || path.getWeight() < shortestPath.getWeight())
                    shortestPath = path;
            }
            String endVertex = shortestPath.getEndVertex();
            for (int i = 0; i < tour.size(); i++) {
                if (tour.get(i).equals(vertexInTour)) {

                }
            }


        }

        return toEdgeList(graph, tour);
    }

    private String vertexOutsideTourFromEdge(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, List<String> tour, DefaultWeightedEdge edge) {
        String v = graph.getEdgeSource(edge);
        if (!tour.contains(v))
            return v;
        return graph.getEdgeTarget(edge);
    }

    private DefaultWeightedEdge minimumWeightEdgePointingOutsideTour(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, List<String> tour) {
        DefaultWeightedEdge closest = null;

        for (String v : Sets.difference(graph.vertexSet(), new HashSet<>(tour))) {
            for (String tourVertex : tour) {
                DefaultWeightedEdge e = graph.getEdge(tourVertex, v);
                if (closest == null || graph.getEdgeWeight(e) < graph.getEdgeWeight(closest))
                    closest = e;
            }
        }
        return closest;
    }

    private Collection<DefaultWeightedEdge> toEdgeList(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, List<String> path) {

        List<DefaultWeightedEdge> l = new ArrayList<>();
        for (int i = 0; i + 1 < path.size(); i++) {
            l.add(graph.getEdge(path.get(i), path.get(i + 1)));
        }
        return l;
    }

    private Collection<String> find3Clique(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        for (String v1 : graph.vertexSet()) {
            for (String v2 : graph.vertexSet()) {
                for (String v3 : graph.vertexSet()) {
                    if (!v1.equals(v2) && !v2.equals(v3)) {
                        if (graph.containsEdge(v1, v2) && graph.containsEdge(v2, v3) && graph.containsEdge(v3, v1))
                            return Arrays.asList(v1, v2, v3);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

}
