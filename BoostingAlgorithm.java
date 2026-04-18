import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class BoostingAlgorithm {
    // number of locations
    private int dim;

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        validateBoost(input, labels, locations, k);
        dim = input.length;
    }

    // validate constructor parameters
    private void validateBoost(int[][] input, int[] labels, Point2D[] locations, int k) {
        // make sure none of the inputs are null
        if (input == null) {
            throw new IllegalArgumentException("input array is null");
        }
        if (locations == null) {
            throw new IllegalArgumentException("weights array is null");
        }
        if (labels == null) {
            throw new IllegalArgumentException("labels array is null");
        }
        // length of input can't be 0
        if (input.length == 0) {
            throw new IllegalArgumentException("length of input cannot be 0");
        }
        // no element in input can have length 0
        for (int i = 0; i < input.length; i++) {
            if (input[i].length == 0) {
                throw new IllegalArgumentException("length of input arrays cannot be 0");
            }
        }
        // k has to be in the range [1, m]
        // inputs is n by m
        if (k < 1 || k > input[0].length) {
            throw new IllegalArgumentException("k is out of range");
        }
        // for an n by k input, the weights array is of length n
        if (locations.length != input.length) {
            throw new IllegalArgumentException("length of weights and input do not match");
        }
        // for an n by k input, the labels array is of length n
        if (labels.length != input.length) {
            throw new IllegalArgumentException("length of labels and input do not match");
        }
        // value of labels are 0 or 1
        for (int i = 0; i < labels.length; i++) {
            if (!(labels[i] == 0 || labels[i] == 1)) {
                throw new IllegalArgumentException("label must be either 1 or 0");
            }
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i > dim - 1) {
            throw new IllegalArgumentException("i needs to be from 0 to " + (dim - 1));
        }
        return 0.0;
    }

    // apply one step of the boosting algorithm
    public void iterate() {
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("sample is null");
        }
        if (sample.length != dim) {
            throw new IllegalArgumentException("length of sample array needs to equal " + dim);
        }
        return -1;
    }

    // unit testing
    public static void main(String[] args) {
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int T = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(
                trainingInput, trainingLabels, trainingLocations, k
        );
        for (int t = 0; t < T; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testingAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testingAccuracy += 1;
        testingAccuracy /= testing.getN();

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testingAccuracy);
    }
}
