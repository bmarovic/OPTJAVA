package part2;


import java.util.*;

public class OffspringSelection {

    private static Random rand = new Random();
    private static int MAX_ITER = 1000;
    private static double ActSelPress = 1;
    private static double MaxSelPress = 500;
    private static double SuccRatio = 0.7;
    private static double compFactor = 0;
    private static int iterCounter = 1;
    private static int kFactor = 4;


    public static void run(IFunction function, List<Chromosome> subPopulation) {

        Collections.sort(subPopulation);
        double lowestError = subPopulation.get(0).getError();
        double ActSelPress = 1;
        double MaxSelPress = 20;
        int iterCounter = 1;
        double compFactor = 0;
        while (ActSelPress < MaxSelPress && iterCounter < MAX_ITER) {

            HashSet<Chromosome> newPopulation = new HashSet<>();
            HashSet<Chromosome> pool = new HashSet<>();
            int newGoodChildren = 0;
            int newBadChildren = 0;
            while (newPopulation.size() < subPopulation.size() * SuccRatio
                    && (newPopulation.size() + pool.size() < subPopulation.size() * MaxSelPress)) {

                Chromosome parent1 = NTournamentSelection.selectFittest(subPopulation, kFactor);
                Chromosome parent2 = NTournamentSelection.selectFittest(subPopulation, kFactor);

                while (true) {
                    if (!parent1.equals(parent2)) {
                        break;
                    }
                    parent2 = NTournamentSelection.selectFittest(subPopulation, kFactor);
                }

                Chromosome child = Crossover.cross(parent1, parent2);
                Mutation.mutate(child);
                child.setError(function.valueAt(child));

                double lowerError;
                double higherError;

                if (parent1.compareTo(parent2) > 0) {
                    lowerError = parent1.getError();
                    higherError = parent2.getError();
                } else {
                    lowerError = parent2.getError();
                    higherError = parent1.getError();
                }

                double fitnessThreshold = higherError + compFactor * (lowerError - higherError);

                if (child.getError() <= fitnessThreshold) {
                    int popSize = newPopulation.size();
                    newPopulation.add(child);
                    newGoodChildren++;

                    if (newPopulation.size() != popSize && child.getError() < lowestError) {
                        lowestError = child.getError();
                        //System.out.println(child);
                    }
                } else {
                    newBadChildren++;
                    pool.add(child);
                }

            }
            ActSelPress = ((double) newGoodChildren + newBadChildren) / subPopulation.size();
            ArrayList<Chromosome> poolList = new ArrayList<>(pool);

            int poolIndex = 0;
            Collections.sort(poolList);
            while (newPopulation.size() != subPopulation.size() && poolList.size() > poolIndex){

                newPopulation.add(poolList.get(poolIndex++));

            }
            int index = 0;
            Collections.shuffle(subPopulation);
            while (newPopulation.size() != subPopulation.size()){
                newPopulation.add(subPopulation.get(index++));
            }

            compFactor += 10.0 / MAX_ITER;
            iterCounter++;

            ArrayList<Chromosome> newPopList = new ArrayList<>(newPopulation);
            for (int i = 0; i < newPopList.size(); i++) {
                subPopulation.set(i, newPopList.get(i));
            }
        }
        System.out.println("Lowest error: " + lowestError);
    }

}
