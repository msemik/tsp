package pl.uj.edu.student.tsp;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by michal on 14.06.15.
 */
public class CoordinateBasedGraphBuilder {
    static class MyPoint2D extends Point2D.Double {

        public MyPoint2D(java.lang.Double x, java.lang.Double y) {
            this.x = x;
            this.y = y;
        }
    }

    Map<String, MyPoint2D> map = new HashMap<>(11000);

    public void addVertex(String v, Double x, Double y) {
        map.put(v, new MyPoint2D(x, y));
    }

    public SimpleWeightedGraph<String, DefaultWeightedEdge> build() {
        SimpleWeightedGraph<String, DefaultWeightedEdge> g = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        for (Map.Entry<String, MyPoint2D> e1 : map.entrySet()) {
            for (Map.Entry<String, MyPoint2D> e2 : map.entrySet()) {
                if (!e1.getKey().equals(e2.getKey())) {
                    Graphs.addEdgeWithVertices(g, e1.getKey(), e2.getKey(), e1.getValue().distance(e2.getValue()));
                }
            }
        }
        return g;
    }
}
