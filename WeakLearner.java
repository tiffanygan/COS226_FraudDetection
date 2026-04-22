import java.util.ArrayList;
import java.util.Comparator;

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
        double weightLeft0 = 0;
        double weightRight0 = 0;
        double weightLeft1 = 0;
        double weightRight1 = 0;
        double maxWeight = Double.NEGATIVE_INFINITY;
        // i dictates Dp (which coordinate we look at)
        for (int i = 0; i < k; i++) {
            index = 0;
            // reset the weights
            weightLeft0 = 0;
            weightRight0 = 0;
            weightLeft1 = 0;
            weightRight1 = 0;
            // go through and collect all the points we are looking at
            for (int j = 0; j < n; j++) {
                values[index] = input[j][i];
                index++;
            }
            // have an arraylist of associated weights and labels
            ArrayList<double[]> combinations = new ArrayList<double[]>();
            for (int h = 0; h < n; h++) {
                double[] curr = new double[] { values[h], weights[h], labels[h] };
                combinations.add(curr);
            }
            // sort the ArrayList by the values
            // nlogn
            combinations.sort(new FirstElementOrder());
            for (int l = 0; l < n; l++) {
                if (combinations.get(l)[2] == 1) {
                    // test Sp = 0
                    // predict 1 for everything above the line
                    weightRight0 += combinations.get(l)[1];
                }
                if (combinations.get(l)[2] == 0) {
                    // test Sp = 1
                    // predict 0 for everything above the line
                    weightRight1 += combinations.get(l)[1];
                }
            }
            if ((weightLeft0 + weightRight0) > maxWeight) {
                dimPredict = i;
                valPredict = (int) combinations.get(0)[0];
                signPredict = 0;
                maxWeight = weightLeft0 + weightRight0;
            }
            if ((weightLeft1 + weightRight1) > maxWeight) {
                dimPredict = i;
                valPredict = (int) combinations.get(0)[0];
                signPredict = 1;
                maxWeight = weightLeft1 + weightRight1;
            }
            // m dictates Vp (partition value)
            for (int m = 0; m < n; m++) {
                if (combinations.get(m)[2] == 0) {
                    // Sp = 0
                    // predict 0 for below or on the line
                    weightLeft0 += combinations.get(m)[1];
                    // Sp = 1
                    // predict 1 for below or on the line
                    weightRight1 -= combinations.get(m)[1];
                }
                if (combinations.get(m)[2] == 1) {
                    // Sp = 0
                    // predict 1 for above the line
                    weightRight0 -= combinations.get(m)[1];
                    // Sp = 1
                    // predict 0 for above the line
                    weightLeft1 += combinations.get(m)[1];
                }
                // if the coordinate value is the same, skip to the end
                if (m < n - 1 && (combinations.get(m)[0] == combinations.get(m + 1)[0])) {
                    continue;
                }
                if ((weightLeft0 + weightRight0) > maxWeight) {
                    dimPredict = i;
                    valPredict = (int) combinations.get(m)[0];
                    signPredict = 0;
                    maxWeight = weightLeft0 + weightRight0;
                }
                if ((weightLeft1 + weightRight1) > maxWeight) {
                    dimPredict = i;
                    valPredict = (int) combinations.get(m)[0];
                    signPredict = 1;
                    maxWeight = weightLeft1 + weightRight1;
                }
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

    private static class FirstElementOrder implements Comparator<double[]> {
        public int compare(double[] a, double[] b) {
            if (a[0] > b[0]) {
                return 1;
            }
            else if (a[0] < b[0]) {
                return -1;
            }
            return 0;
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
        if (signPredict == 1) {
            if (sample[dimPredict] <= valPredict) {
                return 1;
            }
            else {
                return 0;
            }
        }
        if (signPredict == 0) {
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
