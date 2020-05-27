package hr.fer.zemris.optjava.dz12.gp;

import hr.fer.zemris.optjava.dz12.AntTrailGA;
import hr.fer.zemris.optjava.dz12.World;
import hr.fer.zemris.optjava.dz12.nodes.NodeManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private int populationSize;
    private int maxIterations;
    private World world;
    private int contendersInTournament = 7;
    private Random rand = new Random();
    private double plagiarismCoefficient = 0.9;
    private double fitnessThreshold;


    public GeneticAlgorithm(int populationSize, int maxIterations, World world, double fitnessThreshold) {
        this.populationSize = populationSize;
        this.maxIterations = maxIterations;
        this.world = world;
        this.fitnessThreshold = fitnessThreshold;
    }

    public Chromosome run() {

        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize / 10; i++) {
            for (int j = 2; j < 7; j++) {
                population.add(new Chromosome(NodeManagement.generateTree(j,
                        AntTrailGA.MAX_NODES, true)));
                population.add(new Chromosome(NodeManagement.generateTree(j,
                        AntTrailGA.MAX_NODES, false)));
            }
       }

        for (Chromosome member : population) {
            evaluate(member, world);
        }

        List<Chromosome> newPopulation = new ArrayList<>();
        Chromosome best = population.stream().max(Chromosome::compareTo).get();
        for (int i = 0; i < maxIterations; i++) {
            newPopulation.add(best);
            while(newPopulation.size() < populationSize){
                Chromosome parent1;
                Chromosome child;
                double probability = rand.nextDouble();
                if (probability <= 0.01){
                    parent1 = NTournamentSelection.selectFittest(population, contendersInTournament);
                    child = parent1.copy();
                    newPopulation.add(child);
                } else if (probability <= 0.15){
                    parent1 = NTournamentSelection.selectFittest(population, contendersInTournament);
                    child = Mutation.mutate(parent1.copy());
                    evaluate(child, world);
                    if (child.getFitness() == parent1.getFitness()){
                        child.setFitness((int)(child.getFitness() * plagiarismCoefficient));
                    }
                    newPopulation.add(child);
                } else {
                    parent1 = NTournamentSelection.selectFittest(population, contendersInTournament);
                    Chromosome parent2 = NTournamentSelection.selectFittest(population, contendersInTournament);
                    List<Chromosome> children = Crossover.crossover(parent1, parent2);
                    for (Chromosome crossoverChild : children) {
                        if (crossoverChild == null) continue;
                        evaluate(crossoverChild, world);
                        if (crossoverChild.getFitness() == parent1.getFitness()){
                            crossoverChild.setFitness((int)(crossoverChild.getFitness() * plagiarismCoefficient));
                        }
                        newPopulation.add(crossoverChild);
                    }
                }
            }
            Chromosome generationBest = population.stream().max(Chromosome::compareTo).get();
            if (generationBest.getFitness() > best.getFitness() || (generationBest.getFitness() == best.getFitness()
                    && generationBest.getRoot().getSubnodes() < best.getRoot().getSubnodes())) {
                best = generationBest.copy();
                if (best.getFitness() >= fitnessThreshold){
                    System.out.println("Best fitness: " + best.getFitness() + ", number of nodes: " +
                            best.getRoot().getSubnodes());
                    return best;
                }
            }
            population.clear();
            population.addAll(newPopulation);
            newPopulation.clear();
            System.out.println("Best fitness: " + best.getFitness() + ", number of nodes: " +
                    best.getRoot().getSubnodes());
        }
        evaluate(best, world);
        return best;
    }

    public void evaluate(Chromosome chromosome, World world) {
        World worldCopy = world.duplicate();
        while (worldCopy.getSteps() < World.MAX_STEPS) {
            chromosome.getRoot().executeOrder(worldCopy);
        }
        int fitness = worldCopy.getFoodEaten();
        chromosome.setFitness(fitness);
    }

}
