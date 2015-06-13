package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

/**
 * Created by michal on 13.06.15.
 */
public class NearestNeighbourTspSolver implements TspSolver {
    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        Set<String> vertexSet = graph.vertexSet();
        Iterator<String> it = vertexSet.iterator();
        return Collections.emptyList();
    }
}
