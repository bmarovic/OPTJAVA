package hr.fer.zemris.generic.ga;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.rng.RNG;
import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.rngimpl.EVOThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParallelEvaluationGA {

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
    Queue<Chromosome> evaluationQueue = new ConcurrentLinkedQueue<>();
    Queue<Chromosome> finishedQueue = new ConcurrentLinkedQueue<>();

    public ParallelEvaluationGA(RouletteWheelSelection selection, ICrossover crossover, Mutation mutation,
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
                while (true) {
                    Chromosome chromosome = evaluationQueue.poll();
                    if (chromosome == null) continue;
                    if (chromosome.data == null) break;
                    evaluator.evaluate(chromosome);
                    finishedQueue.offer(chromosome);
                }
            });
            workers[i].start();
        }

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
            population.add(new Chromosome(data));
        }
        double bestFitness = Integer.MIN_VALUE;
        Chromosome bestChromosome = population.get(0);

        evaluatePopulation(population);

        int iteration = 0;
        while (bestFitness < errorThreshold && iteration < maxIter) {

            children.add(bestChromosome);

            while (children.size() != populationSize) {
                Chromosome parent1 = selection.selectFittest(population);
                Chromosome parent2 = selection.selectFittest(population);
                Chromosome child = crossover.crossover(parent1, parent2);
                child = mutation.mutate(child);
                children.add(child);
            }

            evaluatePopulation(children);
            children.sort(Chromosome::compareTo);
            if (children.get(0).getFitness() > bestFitness) {
                bestFitness = children.get(0).getFitness();
                bestChromosome = children.get(0);
            }

            population.clear();
            population.addAll(children);
            children.clear();
            iteration++;
            System.out.println(bestFitness + " " + iteration);
        }

        for (int i = 0; i < numberOfWorkers; i++) {
            evaluationQueue.offer(new Chromosome(null));
        }

        return bestChromosome;


    }

    private void evaluatePopulation(List<Chromosome> population) {

        for (Chromosome chromosome : population) {
            evaluationQueue.offer(chromosome);
        }
        population.clear();

        for (int i = 0; i < populationSize; i++) {
            Chromosome temp = finishedQueue.poll();
            if (temp == null) {
                i--;
                continue;
            }
            population.add(temp);
        }
    }
}
