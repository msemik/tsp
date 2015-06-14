package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class NearestInsertionTspSolver implements TspSolver {
    class WeightMatrix {
        private String[] vertices;
        private double[][] weight;

        public SimpleWeightedGraph<String, DefaultWeightedEdge> getGraph() {
            return graph;
        }

        private SimpleWeightedGraph<String, DefaultWeightedEdge> graph;

        public WeightMatrix(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
            this.graph = graph;
            Set<String> v = graph.vertexSet();

            vertices = v.toArray(new String[v.size()]);
            Arrays.sort(vertices);
            weight = new double[vertices.length][vertices.length];

            for (DefaultWeightedEdge edge : graph.edgeSet()) {
                int v1 = getVertex(graph.getEdgeTarget(edge));
                int v2 = getVertex(graph.getEdgeSource(edge));
                if (v1 <= v2)
                    weight[v1][v2] = graph.getEdgeWeight(edge);
                else
                    weight[v2][v1] = graph.getEdgeWeight(edge);
            }
        }

        private int getVertex(String v) {
            return Arrays.binarySearch(vertices, v);
        }

        public double getWeight(int i, int j) {
            if (i <= j)
                return weight[i][j];
            return weight[j][i];
        }

        public String getVertex(int i) {
            return vertices[i];
        }

        public int size() {
            return vertices.length;
        }

        public DefaultWeightedEdge getEdge(int i, int j) {
            return graph.getEdge(getVertex(i), getVertex(j));
        }
    }

    class Tour {
        private boolean[] inTour;
        private List<Integer> tour = new ArrayList<>();
        private WeightMatrix m;

        public Tour(WeightMatrix m) {
            this.m = m;
            inTour = new boolean[m.size()];
            for (int i = 0; i < inTour.length; i++) {
                inTour[i] = false;
            }
        }

        public void appendToTour(Collection<Integer> indexes) {
            for (Integer i : indexes) {
                this.inTour[i] = true;
                tour.add(i);
            }
        }

        public boolean isInTour(int i) {
            return this.inTour[i];
        }

        public int size() {
            return tour.size();
        }

        public Collection<DefaultWeightedEdge> toEdgeList() {
            List<DefaultWeightedEdge> e = new ArrayList<>(tour.size());
            for (int i = 0; i < tour.size(); i++) {
                Integer v1 = tour.get(i);
                Integer v2 = tour.get(i + 1 < tour.size() ? i + 1 : 0);
                e.add(m.getEdge(v1, v2));
            }
            return e;

        }

        public void addAfter(int begin, int newVertex) {
            tour.add(begin+1 % tour.size(), newVertex);
            inTour[newVertex] = true;
        }

        public boolean hasAllVertices() {
            return tour.size() >= m.size();
        }

        public int get(int i) {
            return tour.get(i % tour.size());
        }
    }

    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        WeightMatrix m = new WeightMatrix(graph);
        Tour tour = new Tour(m);
        tour.appendToTour(findSmallest3Clique(m));

        while (!tour.hasAllVertices()) {
            Integer kCandidate = null;
            int begin = 0;
            double minWeight = 0;

            for (int i = 0; i < m.size(); i++)
                for (int k = 0; k < m.size(); k++) {
                    if (!tour.isInTour(k)) {
                        double minWeightCandidate = m.getWeight(tour.get(i), k) + m.getWeight(k, tour.get(i + 1));
                        if (kCandidate == null || minWeightCandidate < minWeight) {
                            kCandidate = k;
                            minWeight = minWeightCandidate;
                            begin = i;
                        }
                    }
                }
            tour.addAfter(begin, kCandidate);
        }
        return tour.toEdgeList();
    }

    private Collection<Integer> findSmallest3Clique(WeightMatrix m) {
        int mini = 0, minj = 2, mink = 1;
        double min = m.getWeight(0, 1) + m.getWeight(1, 2);
        for (int i = 0; i < m.size(); i++) {
            for (int j = i + 1; j < m.size(); j++) {
                for (int k = j + 1; k < m.size(); k++) {
                    double minCandidate = m.getWeight(j, k) + m.getWeight(k, i);
                    if (minCandidate < min) {
                        mini = i;
                        minj = j;
                        mink = k;
                        min = minCandidate;
                    }
                }
            }
        }
        return Arrays.asList(mini, minj, mink);
    }

}
