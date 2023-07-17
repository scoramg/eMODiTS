package TD4C;

public class EntropyDistance extends AbstractDistanceMeasure {


    @Override
    public double cutoffScore(int numberOfClasses, int[][] bins) {
        double entropyDistance = 0;
        for (int i = 0; i < numberOfClasses; i++) {
            for (int j = i + 1; j < numberOfClasses; j++) {
                entropyDistance += Math.abs(entropy(probability(i, bins)) - entropy(probability(j, bins)));
            }
        }
        return entropyDistance;
    }

    @Override
    public String getName() {
        return "Entropy";
    }

    private double entropy(double[] P) {
        double entropy = 0;
        for (double probability : P) {
            entropy += probability * Math.log(probability);
        }
        return -entropy;
    }
}
