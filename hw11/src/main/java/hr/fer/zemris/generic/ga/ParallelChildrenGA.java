package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.RNG;
import hr.fer.zemris.rng.rngimpl.EVOThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelChildrenGA {

    private RouletteWheelSelection selection;
    private ICrossover crossover;
    private Mutation mutation;

    private double errorThreshold;
    private int maxIter;
    private int numberOfRectangles;
    private IRNG rng = RNG.getRNG();
    private GrayScaleImage image;
    private Chromosome bestSolution;
    private int populationSize;

    private List<Chromosome> population = new ArrayList<>();
    private List<Chromosome> children = new ArrayList<>();
    Queue<Integer> evaluationQueue = new ConcurrentLinkedQueue<>();
    Queue<Chromosome> finishedQueue = new ConcurrentLinkedQueue<>();

    public ParallelChildrenGA(RouletteWheelSelection selection, ICrossover crossover, Mutation mutation,
                                double errorThreshold, int maxIter, int numberOfRectangles, GrayScaleImage image,
                                int populationSize) {
        this.selection = selection;
        this.crossover = crossover;
        this.mutation = mutation;
        this.errorThreshold = errorThreshold;
        this.maxIter = maxIter;
        this.numberOfRectangles = numberOfRectangles;
        this.image = image;
        this.populationSize = populationSize;
    }

    public Chromosome run(){

        int numberOfWorkers = Runtime.getRuntime().availableProcessors();
        EVOThread[] workers = new EVOThread[numberOfWorkers];

        for (int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new EVOThread(() -> {
                Evaluator evaluator = new Evaluator(image);
                ICrossover crossover = new KBreakpointCrossover(4);
                while (true) {
                    Integer requiredChildren = evaluationQueue.poll();
                    if (requiredChildren == null) continue;
                    if (requiredChildren == 0) break;
                    for (int j = 0; j < requiredChildren; j++){
                        Chromosome child = childGenerator(crossover);
                        evaluator.evaluate(child);
                        finishedQueue.offer(child);
                    }
                }
            });
            workers[i].start();
        }
        Evaluator evaluator = new Evaluator(image);
        for (int i = 0; i < populationSize; i++) {
            int[] data = new int[1 + numberOfRectangles * 5];
            data[0] = rng.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
            for (int j = 1; j < 1 + numberOfRectangles * 5; j = j + 5) {
                data[j] = rng.nextInt(0, image.getWidth());
                data[j + 1] = rng.nextInt(0, image.getHeight());
                data[j + 2] = rng.nextInt(1, image.getWidth());
                data[j + 3] = rng.nextInt(1, image.getHeight());
                data[j + 4] = rng.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
            }
            Chromosome newMember = new Chromosome(data);
            evaluator.evaluate(newMember);
            population.add(newMember);
        }
        population.sort(Chromosome::compareTo);
        double bestFitness = population.get(0).getFitness();
        Chromosome bestChromosome = population.get(0);

        int iteration = 0;
        int workerRequirement = Math.floorDiv(populationSize, numberOfWorkers);

        while (bestFitness < errorThreshold && iteration < maxIter) {

            children.add(bestChromosome);

            for (int i = 0; i < numberOfWorkers - 1; i++) {
                evaluationQueue.offer(workerRequirement);
            }
            evaluationQueue.offer(populationSize - workerRequirement * (numberOfWorkers - 1));

            while (children.size() != populationSize) {
                Chromosome child = finishedQueue.poll();
                if (child == null) continue;
                children.add(child);
            }

            children.sort(Chromosome::compareTo);
            if (children.get(0).getFitness() > bestFitness) {
                bestFitness = children.get(0).getFitness();
                bestChromosome = children.get(0);
            }

            population = new ArrayList<>(children);
            children.clear();
            iteration++;
            System.out.println(bestFitness + " " + iteration);
        }

        for (int i = 0; i < numberOfWorkers; i++) {
            evaluationQueue.offer(0);
        }

        return bestChromosome;


    }

    private Chromosome childGenerator(ICrossover crossover) {
        Chromosome parent1 = selection.selectFittest(population);
        Chromosome parent2 = selection.selectFittest(population);
        Chromosome child = crossover.crossover(parent1, parent2);
        child = mutation.mutate(child);
        return child;
    }


}
