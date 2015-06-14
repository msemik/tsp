package pl.uj.edu.student.tsp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.*;

public class NearestInsertionTspSolver implements TspSolver {
    private static class InsertionCandidate {
        private DefaultWeightedEdge currentEdge = null;
        private double cost = 0;
        private DefaultWeightedEdge firstNewEdge;
        private DefaultWeightedEdge secondNewEdge;

        public InsertionCandidate(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, String vertex, DefaultWeightedEdge edge) {
            this.currentEdge = edge;
            String edgeSource = graph.getEdgeSource(edge);
            String edgeTarget = graph.getEdgeTarget(edge);
            this.firstNewEdge = graph.getEdge(edgeSource, vertex);
            this.secondNewEdge = graph.getEdge(vertex, edgeTarget);
            this.cost = graph.getEdgeWeight(firstNewEdge) + graph.getEdgeWeight(secondNewEdge);
        }

        public DefaultWeightedEdge getCurrentEdge() {
            return currentEdge;
        }

        public boolean isBetterThan(InsertionCandidate candidate) {
            return cost < candidate.cost;
        }

        public DefaultWeightedEdge getFirstNewEdge() {
            return firstNewEdge;
        }

        public DefaultWeightedEdge getSecondNewEdge() {
            return secondNewEdge;
        }

        @Override
        public String toString() {
            return "InsertionCandidate{" +
                    "currentEdge=" + currentEdge +
                    ", cost=" + cost +
                    ", firstNewEdge=" + firstNewEdge +
                    ", secondNewEdge=" + secondNewEdge +
                    '}';
        }
    }

    @Override
    public Collection<DefaultWeightedEdge> solve(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        List<DefaultWeightedEdge> tour = new LinkedList<>();
        Set<String> verticesInTour = new HashSet<>();
        tour.addAll(findSmallest3Clique(graph));

        for (DefaultWeightedEdge edge : tour) {
            addEdgeVerticesToTour(graph, verticesInTour, edge);
        }

        while (tour.size() < graph.vertexSet().size()) {
            InsertionCandidate bestCandidate = null;
            for (DefaultWeightedEdge edge : tour) {
                for (String v : graph.vertexSet()) {
                    if (vertexOutsideTour(verticesInTour, v)) {
                        if (bestCandidate == null)
                            bestCandidate = new InsertionCandidate(graph, v, edge);
                        else {
                            InsertionCandidate candidate = new InsertionCandidate(graph, v, edge);
                            if (candidate.isBetterThan(bestCandidate))
                                bestCandidate = candidate;
                        }
                    }
                }
            }
            addCandidateToTour(graph, tour, bestCandidate);
            addEdgeVerticesToTour(graph, verticesInTour, bestCandidate.getFirstNewEdge());
            addEdgeVerticesToTour(graph, verticesInTour, bestCandidate.getSecondNewEdge());
        }
        return tour;
    }

    private void addEdgeVerticesToTour(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, Set<String> verticesInTour, DefaultWeightedEdge edge) {
        verticesInTour.add(graph.getEdgeTarget(edge));
        verticesInTour.add(graph.getEdgeSource(edge));
    }

    private boolean vertexOutsideTour(Set<String> verticesInTour, String v) {
        return !verticesInTour.contains(v);
    }

    private void addCandidateToTour(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, List<DefaultWeightedEdge> tour, InsertionCandidate candidate) {
        int i = tour.indexOf(candidate.getCurrentEdge());
        DefaultWeightedEdge nextEdge = tour.get(i + 1 < tour.size() ? i + 1 : 0);
        if (hasCommonVertex(graph, candidate.getSecondNewEdge(), nextEdge)) {
            tour.set(i, candidate.getFirstNewEdge());
            tour.add(i + 1, candidate.getSecondNewEdge());
        } else {
            tour.set(i, candidate.getSecondNewEdge());
            tour.add(i + 1, candidate.getFirstNewEdge());
        }
    }

    private boolean hasCommonVertex(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, DefaultWeightedEdge e1, DefaultWeightedEdge e2) {
        String e1Source = graph.getEdgeSource(e1);
        String e1Target = graph.getEdgeTarget(e1);
        String e2Source = graph.getEdgeSource(e2);
        String e2Target = graph.getEdgeTarget(e2);
        return e1Source.equals(e2Source) || e1Source.equals(e2Target) || e1Target.equals(e2Source) || e1Target.equals(e2Target);
    }

    private Collection<DefaultWeightedEdge> findSmallest3Clique(SimpleWeightedGraph<String, DefaultWeightedEdge> graph) {
        class CliqueCandidate {
            private DefaultWeightedEdge e1;
            private DefaultWeightedEdge e2;
            private DefaultWeightedEdge e3;
            private double cost = 0;

            CliqueCandidate(SimpleWeightedGraph<String, DefaultWeightedEdge> graph, String v1, String v2, String v3) {
                e1 = graph.getEdge(v1, v2);
                e2 = graph.getEdge(v2, v3);
                e3 = graph.getEdge(v3, v1);
                cost = graph.getEdgeWeight(e1) + graph.getEdgeWeight(e2) + graph.getEdgeWeight(e3);
            }

            public boolean isSmallerThan(CliqueCandidate cliqueCandidate) {
                return cost < cliqueCandidate.cost;

            }

            public Collection<DefaultWeightedEdge> toEdgeList() {
                return Arrays.asList(e1, e2, e3);
            }

            @Override
            public String toString() {
                return "CliqueCandidate{" +
                        "e1=" + e1 +
                        ", e2=" + e2 +
                        ", e3=" + e3 +
                        ", cost=" + cost +
                        '}';
            }
        }

        CliqueCandidate bestCandidate = null;
        for (String v1 : graph.vertexSet()) {
            for (String v2 : graph.vertexSet()) {
                for (String v3 : graph.vertexSet()) {
                    if (!v1.equals(v2) && !v2.equals(v3) && ! v1.equals(v3)) {
                        CliqueCandidate candidate = new CliqueCandidate(graph, v1, v2, v3);
                        if (bestCandidate == null || candidate.isSmallerThan(bestCandidate))
                            bestCandidate = candidate;
                    }
                }
            }
        }
        return bestCandidate == null ? Collections.emptyList() : bestCandidate.toEdgeList();
    }

}
