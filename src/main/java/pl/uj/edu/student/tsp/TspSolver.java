package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by michal on 11.06.15.
 */
public interface TspSolver {
    Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph);
}
