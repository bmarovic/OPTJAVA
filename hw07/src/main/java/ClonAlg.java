import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class ClonAlg{

    private int velPop;
    private int maxIter;
    private double errorThreshold;
    private FFANN ffann;
    private static int randomUnitsPerIteration = 20;
    private static double cloneNumberParameter = 5.0;
    Random rand = new Random();
    private final double mutationFactor = 0.1;
    private final int maxAge = 20;
    private final double xmin = -1;
    private final double xmax = 1;


    public ClonAlg(int velPop, int maxIter, double errorThreshold, FFANN ffann) {
        this.velPop = velPop;
        this.maxIter = maxIter;
        this.errorThreshold = errorThreshold;
        this.ffann = ffann;
    }

    public void run() {

        List<WeightVectorSolution> population = new ArrayList<>();
        List<WeightVectorSolution> clonePopulation = new ArrayList<>();

        for (int i = 0; i < velPop; i++) {
            WeightVectorSolution randomSolution = new WeightVectorSolution(ffann.getWeightsCount());
            randomSolution.randomize(rand, xmin, xmax);
            population.add(randomSolution);
        }

        int iterCounter = 0;
        while (iterCounter < maxIter){

            for (WeightVectorSolution solution : population) {
                solution.setError(ffann.calculateError(solution.getValues()));
                solution.increaseAge();
            }
            population.sort(WeightVectorSolution::compareTo);
            population.get(0).resetAge();

            for (int i = 1; i < population.size(); i++) {
                int numberOfClones = (int) Math.floor((cloneNumberParameter * velPop) / i);
                for (int j = 0; j < numberOfClones; j++) {
                    clonePopulation.add(population.get(i - 1).duplicate());
                }
            }

            for (WeightVectorSolution weightVectorSolution : clonePopulation) {
                double mutationRate = 1 - Math.exp(-mutationFactor / weightVectorSolution.getError());
                weightVectorSolution.mutate(mutationRate, rand);
            }

            for (WeightVectorSolution solution : clonePopulation) {
                solution.setError(ffann.calculateError(solution.getValues()));
            }
            clonePopulation.sort(WeightVectorSolution::compareTo);

            population.clear();
            for (int i = 0; i < velPop; i++) {
                population.add(clonePopulation.get(i));
            }

            population = population.stream()
                    .filter(x -> x.getAge() < maxAge)
                    .sorted()
                    .limit(velPop - randomUnitsPerIteration)
                    .collect(Collectors.toList());

            for (int i = 0; i < randomUnitsPerIteration; i++) {
                WeightVectorSolution randomSolution = new WeightVectorSolution(ffann.getWeightsCount());
                randomSolution.randomize(rand, xmin, xmax);
                population.add(randomSolution);
            }
            for (WeightVectorSolution solution : population) {
                solution.setError(ffann.calculateError(solution.getValues()));
                solution.increaseAge();
            }
            System.out.println("Error: " + population.get(0).getError());
            iterCounter++;
            if (population.get(0).getError() < errorThreshold) break;
        }

        for (WeightVectorSolution solution : population) {
            solution.setError(ffann.calculateError(solution.getValues()));
        }

        population.sort(WeightVectorSolution::compareTo);
        System.out.println("Minimal error: " + population.get(0).getError());
        ffann.printStatistics(population.get(0).getValues());
    }



}
