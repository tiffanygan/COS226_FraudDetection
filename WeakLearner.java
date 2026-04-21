public class WeakLearner {
    // number of locations
    private int k;
    // dimension predictor
    // indicates the j in input[i][j]
    private int dimPredict;
    // value predictor
    // indicates which value to split at
    private int valPredict;
    // sign predictor
    // indicates which side to split in
    private int signPredict;

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        validateWeakLearner(input, weights, labels);
        int n = input.length;
        k = input[0].length;
        int[] values = new int[n];
        int index = 0;
        double weight0 = 0;
        double weight1 = 0;
        double maxWeight = Double.NEGATIVE_INFINITY;
        // i dictates Dp (which coordinate we look at)
        for (int i = 0; i < k; i++) {
            index = 0;
            // go through and collect all the points we are looking at
            for (int j = 0; j < n; j++) {
                values[index] = input[j][i];
                index++;
            }
            // m dictates Vp (partition value)
            for (int m = 0; m < n; m++) {
                for (int w = 0; w < n; w++) {
                    if (values[w] <= values[m]) {
                        // if Sp = 0
                        if (labels[w] == 0) {
                            weight0 += weights[w];
                        }
                        // if Sp = 1
                        if (labels[w] == 1) {
                            weight1 += weights[w];
                        }
                    }
                    else {
                        // if Sp = 0
                        if (labels[w] == 1) {
                            weight0 += weights[w];
                        }
                        // if Sp = 1
                        if (labels[w] == 0) {
                            weight1 += weights[w];
                        }
                    }
                }
                if (weight0 > maxWeight) {
                    dimPredict = i;
                    valPredict = values[m];
                    signPredict = 0;
                    maxWeight = weight0;
                }
                if (weight1 > maxWeight) {
                    dimPredict = i;
                    valPredict = values[m];
                    signPredict = 1;
                    maxWeight = weight1;
                }
                weight0 = 0;
                weight1 = 0;
            }
        }
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
            if (weights[i] < 0) {
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
            throw new IllegalArgumentException("sample is null");
        }
        if (sample.length != k) {
            throw new IllegalArgumentException("length of sample array needs to equal " + k);
        }
        if (signPredict == 0) {
            if (sample[dimPredict] <= valPredict) {
                return 1;
            }
            else {
                return 0;
            }
        }
        if (signPredict == 1) {
            if (sample[dimPredict] <= valPredict) {
                return 0;
            }
            else {
                return 1;
            }
        }
        return 0;
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
