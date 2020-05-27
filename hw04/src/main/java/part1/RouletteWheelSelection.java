package part1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RouletteWheelSelection {

    private final static double SAMPLE_PERCENTAGE = 0.4;

    public static Chromosome selectFittest(ArrayList<Chromosome> population){
        Random rand = new Random();
        Collections.sort(population);
        double worstFitness = population.get((int) (Math.round(population.size() * SAMPLE_PERCENTAGE) - 1)).fitness;
        ArrayList<Double> fitnessList = new ArrayList<>();

        for (int i = 0; i < Math.round(population.size() * SAMPLE_PERCENTAGE) ; i++) {
            fitnessList.add((population.get(i).fitness - worstFitness) * -1);
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
        for (int i = 0; i < Math.round(population.size() * SAMPLE_PERCENTAGE) - 1; i++) {
            if (randomValue > fitnessList.get(i) && randomValue < fitnessList.get(i + 1)) return population.get(i);
        }
        return population.get((int) (Math.round(population.size() * SAMPLE_PERCENTAGE) - 1));
    }

}
