public class WeakLearner {
    // dimension predictor
    private int dimPredict;
    // value predictor
    private int valPredict;
    // sign predictor
    private int signPredict;

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        validateWeakLearner(input, weights, labels);
    }

    // validate constructor parameters
    private void validateWeakLearner(int[][] input, double[] weights, int[] labels) {
        // make sure none of the inputs are null
        if (input == null) {
            throw new IllegalArgumentException("input array is null");
        }
        if (weights == null) {
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
        // for an n by k input, the weights array is of length n
        if (weights.length != input.length) {
            throw new IllegalArgumentException("length of weights and input do not match");
        }
        // non-negative weights
        for (int i = 0; i < weights.length; i++) {
            if (weights[i] <= 0) {
                throw new IllegalArgumentException("weights need to be non-negative");
            }
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

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new RuntimeException("sample is null");
        }
        return -1;
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dimPredict;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return valPredict;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return signPredict;
    }

    // unit testing
    public static void main(String[] args) {
    }
}
