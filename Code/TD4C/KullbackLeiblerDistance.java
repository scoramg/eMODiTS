package TD4C;

public class KullbackLeiblerDistance extends AbstractDistanceMeasure {


    @Override
    public double cutoffScore(int numberOfClasses, int[][] bins) {
        double klDistance = 0;
        for (int i = 0; i < numberOfClasses; i++) {
            for (int j = i + 1; j < numberOfClasses; j++) {
                klDistance += skl(probability(i, bins), probability(j, bins));
            }
        }
        return klDistance;
    }

    @Override
    public String getName() {
        return "Kullback Leibler";
    }

    private double kl(double[] P, double[] Q) {
        double sum = 0;
        for (int i = 0; i < P.length; i++) {
            sum += P[i] * Math.log(P[i] / Q[i]);
        }
        return sum;
    }

    private double skl(double[] P, double[] Q) {
        return (kl(P, Q) + kl(Q, P)) / 2;
    }
}
