package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class GraphBuilder {
    SimpleWeightedGraph<String, DefaultWeightedEdge> graph;

    public GraphBuilder() {
        graph = new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
    }

    public GraphBuilder addEdge(String v1, String v2, double weight) {
        if (!graph.containsVertex(v1.trim()))
            graph.addVertex(v1.trim());

        if (!graph.containsVertex(v2.trim()))
            graph.addVertex(v2.trim());

        graph.setEdgeWeight(graph.addEdge(v2, v1), weight);
        return this;
    }

    public SimpleWeightedGraph<String, DefaultWeightedEdge> build() {
        return graph;
    }
}