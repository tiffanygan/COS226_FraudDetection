import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BoostingAlgorithm {
    // uncompressed dimension
    private int m;
    // length of inputs
    private int n;
    // weights (length n)
    private double[] weights;
    // sum of weights (should be 1 after normalization)
    private double weightsSum = 1;
    // compressed input nxk
    private int[][] input;
    // labels
    private int[] labels;
    // Clustering object
    private Clustering clustering;
    // WeakLearner object
    private ArrayList<WeakLearner> weakLearners;

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        validateBoost(input, labels, locations, k);

        // uncompressed dimension
        m = input[0].length;
        this.n = input.length;
        this.labels = labels.clone();
        weakLearners = new ArrayList<WeakLearner>();

        clustering = new Clustering(locations, k);
        // compress input to a nxk array
        this.input = new int[n][k];
        for (int i = 0; i < n; i++) {
            this.input[i] = clustering.reduceDimensions(input[i]);
        }

        // initialize all weights to 1/n
        weights = new double[n];
        for (int i = 0; i < n; i++) weights[i] = (double) 1 / n;
    }

    // validate constructor parameters
    private void validateBoost(
            int[][] inputCheck, int[] labelsCheck, Point2D[] locations, int k) {
        // make sure none of the inputs are null
        if (inputCheck == null) {
            throw new IllegalArgumentException("input array is null");
        }
        if (locations == null) {
            throw new IllegalArgumentException("weights array is null");
        }
        if (labelsCheck == null) {
            throw new IllegalArgumentException("labels array is null");
        }
        // length of input can't be 0
        if (inputCheck.length == 0) {
            throw new IllegalArgumentException("length of input cannot be 0");
        }
        // no element in input can have length 0
        for (int i = 0; i < inputCheck.length; i++) {
            if (inputCheck[i].length == 0) {
                throw new IllegalArgumentException(
                        "length of input arrays cannot be 0");
            }
        }
        // k has to be in the range [1, m]
        // inputs is n by m
        if (k < 1 || k > inputCheck[0].length) {
            throw new IllegalArgumentException("k is out of range");
        }
        // for an n by k input, the locations array is of length m
        if (locations.length != inputCheck[0].length) {
            throw new IllegalArgumentException(
                    "length of locations and input do not match");
        }
        // for an n by k input, the labels array is of length n
        if (labelsCheck.length != inputCheck.length) {
            throw new IllegalArgumentException(
                    "length of labels and input do not match");
        }
        // value of labels are 0 or 1
        for (int i = 0; i < labelsCheck.length; i++) {
            if (!(labelsCheck[i] == 0 || labelsCheck[i] == 1)) {
                throw new IllegalArgumentException("label must be either 1 or 0");
            }
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException(
                    "i needs to be from 0 to " + (n - 1));
        }
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        // create weak learner
        WeakLearner weakLearner = new WeakLearner(input, weights, labels);

        // train model
        for (int i = 0; i < n; i++) {
            // get prediction
            int prediction = weakLearner.predict(input[i]);

            // double weights of mislabeled inputs
            if (labels[i] != prediction) {
                weightsSum += weights[i];
                weights[i] *= 2;
            }
        }

        // renormalize
        for (int i = 0; i < n; i++) {
            weights[i] /= weightsSum;
        }
        weightsSum = 1;

        weakLearners.add(weakLearner);
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("sample is null");
        }
        // sample has to be of length m (uncompressed)
        if (sample.length != m) {
            throw new IllegalArgumentException(
                    "length of sample array needs to equal " + m);
        }

        // call Clustering.reduceDimensions() once
        sample = clustering.reduceDimensions(sample);

        int numZeros = 0;
        int numOnes = 0;
        // call WeakLearner.predict() for every weakLearner we have
        for (WeakLearner weakLearner : weakLearners) {
            if (weakLearner.predict(sample) == 0) numZeros++;
            else numOnes++;
        }

        // return majority-vote prediction
        // in case of tie, return 0
        if (numZeros >= numOnes) return 0;
        return 1;
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
