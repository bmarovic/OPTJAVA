package hr.fer.zemris.generic.ga;

import hr.fer.zemris.rng.RNG;
import hr.fer.zemris.rng.IRNG;

import java.util.*;

public class RouletteWheelSelection {

    private final static int SAMPLE_SIZE = 10;
    private IRNG rand = RNG.getRNG();

    public Chromosome selectFittest(List<Chromosome> population){

        List<Chromosome> tempPopulation = new ArrayList<>(population);
        tempPopulation.sort(Chromosome::compareTo);
        double worstFitness = tempPopulation.get(SAMPLE_SIZE - 1).fitness;
        List<Double> fitnessList = new ArrayList<>();

        for (int i = 0; i < SAMPLE_SIZE ; i++) {
            fitnessList.add((tempPopulation.get(i).fitness - worstFitness) * -1);
        }

        double fitnessSum = 0;

        for (Double fitness : fitnessList) {
            fitnessSum += fitness;
        }

        double previousProbability = 0;

        for (int i = 0; i < fitnessList.size(); i++) {
            double tempProb = previousProbability + fitnessList.get(i) / fitnessSum;
            fitnessList.set(i, previousProbability);
            previousProbability = tempProb;
        }

        double randomValue = rand.nextDouble();
        for (int i = 0; i < SAMPLE_SIZE - 1; i++) {
            if (randomValue > fitnessList.get(i) && randomValue < fitnessList.get(i + 1)) return tempPopulation.get(i);
        }
        return tempPopulation.get(SAMPLE_SIZE - 1);
    }

}
