package part1;

import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

    double[] values;
    double fitness;

    public Chromosome(int numberOfValues) {
        this.values = new double[numberOfValues];
    }

    public void randomize(Random rand, double[] lowerLimits, double[] upperLimits){

        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextDouble() * (upperLimits[i] - lowerLimits[i]) + lowerLimits[i];
        }
    }

    public int numberOfVariables(){
        return values.length;
    }

    public double getAtIndex(int index){
        return values[index];
    }

    public void setAtIndex(double value, int index){
        values[index] = value;
    }

    @Override
    public String toString() {
        return "part1.DoubleArraySolution{" +
                "values=" + Arrays.toString(values) +
                ", fitness=" + fitness +
                '}';
    }

    @Override
    public int compareTo(Chromosome solution){
        return Double.compare(this.fitness, solution.fitness);
    }

}
