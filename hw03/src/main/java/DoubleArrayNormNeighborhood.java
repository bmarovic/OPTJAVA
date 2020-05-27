import java.util.Random;

public class DoubleArrayNormNeighborhood implements INeighborhood<DoubleArraySolution> {
    private double[] deltas;
    private Random rand = new Random();

    public DoubleArrayNormNeighborhood(double[] deltas) {
        this.deltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {
        int numberOfVariables = solution.values.length;
        DoubleArraySolution randomizedSolution = new DoubleArraySolution(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {

                randomizedSolution.values[i] = solution.values[i] + rand.nextGaussian()*deltas[i];

        }
        return randomizedSolution;
    }
}
