import java.util.Random;

public class DoubleArraySolution extends SingleObjectiveSolution{

    double[] values;

    public DoubleArraySolution(int numberOfValues) {
        this.values = new double[numberOfValues];
    }

    public DoubleArraySolution newLikeThis(){
        return new DoubleArraySolution(values.length);
    }

    public DoubleArraySolution duplicate(){
        DoubleArraySolution copy = new DoubleArraySolution(values.length);
        System.arraycopy(this.values, 0, copy.values, 0, values.length);
        return copy;
    }

    public void randomize(Random rand, double[] lowerLimits, double[] upperLimits){

        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextDouble() * (upperLimits[i] - lowerLimits[i]) + lowerLimits[i];
        }
    }
}
