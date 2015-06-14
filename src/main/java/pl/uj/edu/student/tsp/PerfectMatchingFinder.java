package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * Created by alanhawrot
 */
public interface PerfectMatchingFinder {

    SimpleWeightedGraph<String, DefaultWeightedEdge> findPerfectMatching(SimpleWeightedGraph<String, DefaultWeightedEdge> graph);
}
