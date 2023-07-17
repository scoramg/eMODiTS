package TD4C;

public interface IDistanceMeasure {

    public double cutoffScore(int numberOfClasses, int[][] bins);

    String getName();
}
