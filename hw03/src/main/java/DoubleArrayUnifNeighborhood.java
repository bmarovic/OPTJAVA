import java.util.Random;

public class DoubleArrayUnifNeighborhood implements INeighborhood<DoubleArraySolution>{

    private double[] deltas;
    private Random rand = new Random();

    public DoubleArrayUnifNeighborhood(double[] deltas) {
        this.deltas = deltas;
    }

    @Override
    public DoubleArraySolution randomNeighbor(DoubleArraySolution solution) {

        double probability = 0.3;
        int numberOfVariables = solution.values.length;
        DoubleArraySolution randomizedSolution = new DoubleArraySolution(numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            if (probability > rand.nextDouble()){
                randomizedSolution.values[i] = solution.values[i]
                        + rand.nextDouble()*(2 * deltas[i]) - deltas[i];
            }
        }
        return randomizedSolution;
    }
}
