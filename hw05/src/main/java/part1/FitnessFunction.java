package part1;

public class FitnessFunction implements IFunction {

    @Override
    public double valueAt(Chromosome chromosome) {

        int k = 0;
        int n = chromosome.getBits().length;

        for (boolean bit : chromosome.getBits()) {
            if (bit) k++;
        }

        if (k <= 0.8 * n) return ((double)k) / n;
        else if (k <= 0.9 * n) return 0.8;
        else return (2 * ((double)k) / n) - 1;
    }

}
