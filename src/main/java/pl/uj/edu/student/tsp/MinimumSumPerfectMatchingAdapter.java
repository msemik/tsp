package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.builder.UndirectedWeightedGraphBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alanhawrot
 */
public class MinimumSumPerfectMatchingAdapter implements PerfectMatchingFinder {

    private MinimumSumPerfectMatchingFinder finder = new MinimumSumPerfectMatchingFinder();

    @Override
    public SimpleWeightedGraph<String, DefaultWeightedEdge> findPerfectMatching(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        if (graph.vertexSet().size() <= 2) {
            return graph;
        }

        int n = graph.vertexSet().size();
        double weight[][]= new double[n + 1][n + 1];
        int sol[] = new int[n + 1];

        List<String> vertices = new ArrayList<>(graph.vertexSet());

        for (int i = 1; i < n + 1; i++) {
            weight[i][i] = 0.0d;
            for (int j = i + 1; j < n + 1; j++) {
                weight[i][j] = graph.getEdgeWeight(graph.getEdge(vertices.get(i - 1), vertices.get(j - 1)));
            }
        }

        finder.minSumMatching(n, weight, sol);

        UndirectedWeightedGraphBuilder<String, DefaultWeightedEdge, SimpleWeightedGraph<String, DefaultWeightedEdge>> undirectedWeightedGraphBuilder
                = new UndirectedWeightedGraphBuilder<>(new SimpleWeightedGraph<>(DefaultWeightedEdge.class));

        for (int i = 1; i <= n; i++) {
            String source = vertices.get(i - 1);
            String target = vertices.get(sol[i] - 1);

            undirectedWeightedGraphBuilder.addEdge(source, target, graph.getEdgeWeight(graph.getEdge(source, target)));
        }

        return undirectedWeightedGraphBuilder.build();
    }
}
