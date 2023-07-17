package TD4C;

import java.util.stream.IntStream;

abstract class AbstractDistanceMeasure implements IDistanceMeasure {

    protected double[] probability(int classPointer, int[][] bins) {
        double[] probability = new double[bins[classPointer].length];
        for (int i = 0; i < bins[classPointer].length; i++) {
            probability[i] = (bins[classPointer][i] + 1.0) / (IntStream.of(bins[classPointer]).sum() + bins.length);
        }
        return probability;
    }
}
