package part1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class GeneticAlgorithm {

    private static int N;
    private static int MIN_POP = 5;
    private static int MAX_POP = 100;
    private static int MAX_EFFORT = 2000;
    private static int MAX_ITER = 100;
    private static int compFactor = 0;
    private static int kFactor = 2;
    private static boolean oneRandom = true;
    private static Random rand = new Random();

    public static void main(String[] args) {

        if (args.length != 1) System.exit(1);
        N = Integer.parseInt(args[0]);
        if (N < 1) System.exit(1);

        ArrayList<Chromosome> population = new ArrayList<>();
        IFunction function = new FitnessFunction();

        while (population.size() != MAX_POP) {
            Chromosome chromosome = new Chromosome(N);
            chromosome.randomize(rand);
            chromosome.setFitness(function.valueAt(chromosome));
            if(!population.contains(chromosome)) population.add(chromosome);
        }

        double maxFitness = Collections.max(population).getFitness();

        int iterCounter = 0;
        while(iterCounter < MAX_ITER && population.size() > MIN_POP){
            HashSet<Chromosome> newPopulation = new HashSet<>();
            int effortCounter = 0;
            while(newPopulation.size() < MAX_POP && effortCounter < MAX_EFFORT){

                Chromosome parent1 = NTournamentSelection.selectFittest(population, kFactor);
                Chromosome parent2 = null;
                if(oneRandom){
                    boolean duplicate = true;
                    while (duplicate){
                        int randInt = rand.nextInt(population.size() - 1);
                        parent2 = population.get(randInt);
                        if (!parent1.equals(parent2)){
                            duplicate = false;
                        }
                    }
                }else{
                    boolean duplicate = true;
                    while (duplicate){
                        parent2 = NTournamentSelection.selectFittest(population, kFactor);
                        if(!parent1.equals(parent2)){
                            duplicate = false;
                        }
                    }
                }
                Chromosome child = Crossover.cross(parent1, parent2);
                Mutation.mutate(child);
                child.setFitness(function.valueAt(child));
                double betterFitness;
                double worseFitness;
                if(parent1.compareTo(parent2) > 0){
                    betterFitness = parent1.getFitness();
                    worseFitness = parent2.getFitness();
                }else {
                    betterFitness = parent2.getFitness();
                    worseFitness = parent1.getFitness();
                }
                double fitnessThreshold = worseFitness + compFactor * (betterFitness - worseFitness);
                if (child.getFitness() > fitnessThreshold){
                    int popSize = newPopulation.size();
                    newPopulation.add(child);
                    if(newPopulation.size() != popSize && child.getFitness() > maxFitness) {
                        maxFitness = child.getFitness();
                        System.out.println(child);
                    }
                }
                effortCounter++;
            }
            compFactor += 5.0 / MAX_ITER;
            iterCounter++;
            population = new ArrayList<>(newPopulation);
        }
    }

}
