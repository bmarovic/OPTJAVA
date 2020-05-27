import java.util.*;

public class RouletteWheelSelection {

    private final static double SAMPLE_PERCENTAGE = 0.6;

    public static Chromosome selectFittest(List<Chromosome> population){
        Random rand = new Random();
        population.sort(Comparator.reverseOrder());
        double worstFitness = population.get((int) (Math.round(population.size() * SAMPLE_PERCENTAGE) - 1)).getFitness();
        List<Double> fitnessList = new ArrayList<>();

        for (int i = 0; i < Math.round(population.size() * SAMPLE_PERCENTAGE) ; i++) {
            fitnessList.add((population.get(i).getFitness() - worstFitness) * -1);
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
