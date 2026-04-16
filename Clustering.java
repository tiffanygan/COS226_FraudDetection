import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

public class Clustering {

    // digraph representing the locations
    private EdgeWeightedGraph graph;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) {
            throw new IllegalArgumentException("Array is null");
        }
        if (k < 1 || k > locations.length) {
            throw new IllegalArgumentException("k is out of bounds");
        }
        // set up the graph
        graph = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < locations.length; i++) {
            for (int j = i + 1; j < locations.length; j++) {
                Edge newEdge = new Edge(i, j, locations[i].distanceTo(locations[j]));
                graph.addEdge(newEdge);
            }
        }
        KruskalMST mst = new KruskalMST(graph);
    }

    // return the cluster of the ith location
    public int clusterOf(int i) {
        return -1;
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        return null;
    }

    // unit testing (optional)
    public static void main(String[] args) {
    }
}
