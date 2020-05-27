package part1;

import java.util.Arrays;
import java.util.Random;

public class Chromosome implements Comparable<Chromosome> {

    private boolean[] bits;
    private double fitness;

    public Chromosome(int numberOfBits) {
        this.bits = new boolean[numberOfBits];
    }

    public void randomize(Random rand){
        for (int i = 0; i < bits.length; i++) {
            bits[i] = rand.nextBoolean();
        };
    }

    public boolean[] getBits() {
        return bits;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return fitness;
    }

    public int numberOfBits() {
        return bits.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Arrays.equals(bits, that.bits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bits);
    }

    public int compareTo(Chromosome solution){
        return Double.compare(this.fitness, solution.fitness);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Boolean bit : bits) {
            if(bit) sb.append(1);
            else sb.append(0);
        }
        sb.append("  Fitness:").append(fitness);
        return sb.toString();
    }
}
