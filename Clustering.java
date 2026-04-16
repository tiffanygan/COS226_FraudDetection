import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class Clustering {

    // digraph representing the locations
    private EdgeWeightedGraph graph;
    // number of clusters
    private int k;
    // number of locations
    private int m;
    // clusters
    private CC clusters;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        // validate parameters
        if (locations == null) {
            throw new IllegalArgumentException("Array is null");
        }
        if (k < 1 || k > locations.length) {
            throw new IllegalArgumentException("k is out of bounds");
        }
        this.k = k;
        this.m = locations.length;
        // set up the graph
        graph = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < locations.length; i++) {
            for (int j = i + 1; j < locations.length; j++) {
                // each edge is Euclidean distance
                Edge newEdge = new Edge(i, j, locations[i].distanceTo(locations[j]));
                graph.addEdge(newEdge);
            }
        }
        // get minimum spanning tree
        KruskalMST mst = new KruskalMST(graph);
        ArrayList<Edge> mstEdges = new ArrayList<Edge>();
        for (Edge e : mst.edges()) {
            mstEdges.add(e);
        }
        Collections.sort(mstEdges);
        // remove the largest k-1 edges
        // the remaining ones are the m-k lowest weights
        for (int i = 0; i < k - 1; i++) {
            mstEdges.remove(mstEdges.size() - 1);
        }
        // create a new graph without those edges
        EdgeWeightedGraph newGraph = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < mstEdges.size(); i++) {
            newGraph.addEdge(mstEdges.get(i));
        }
        // create the clusters graph
        clusters = new CC(newGraph);
    }

    // return the cluster of the ith location
    public int clusterOf(int i) {
        return clusters.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        int[] newTransactions = new int[k];
        int index;
        for (int i = 0; i < m; i++) {
            // get the appropriate cluster
            index = clusterOf(i);
            // add the transaction weight
            newTransactions[index] += input[i];
        }
        return newTransactions;
    }

    // unit testing (optional)
    public static void main(String[] args) {
    }
}
