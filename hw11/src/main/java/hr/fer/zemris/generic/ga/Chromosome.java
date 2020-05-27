package hr.fer.zemris.generic.ga;

import java.util.Arrays;

public class Chromosome extends GASolution<int[]> {

    public Chromosome(int[] data) {
        super.data = data;
    }

    @Override
    public GASolution<int[]> duplicate() {
        return new Chromosome(Arrays.copyOf(super.data, super.data.length));
    }

    public void setAtIndex(int value, int i) {
        super.data[i] = value;
    }

    public int getAtIndex(int i) {
        return super.data[i];
    }

    public int numberOfVariables() {
        return super.data.length;
    }

    public double getFitness() {
        return super.fitness;
    }


}
