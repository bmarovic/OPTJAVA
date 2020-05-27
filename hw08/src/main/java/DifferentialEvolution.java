import java.util.Random;

public class DifferentialEvolution {

    private double[][] population;
    private double[] errors;
    private int maxIter;
    private int velPop;
    private double errorThreshold;
    private NeuralNetwork neuralNetwork;
    private Random rand = new Random();
    private final static double upperLimit = 1.0;
    private final static double lowerLimit = -1.0;
    private final static double scalarF = 0.7;
    private final static double probability = 0.7;

    //Najbolje rezultate daje DE/target-to-best/1/exp
    public DifferentialEvolution(int maxIter, int velPop, double errorThreshold, NeuralNetwork neuralNetwork) {
        this.maxIter = maxIter;
        this.velPop = velPop;
        this.errorThreshold = errorThreshold;
        this.neuralNetwork = neuralNetwork;
        this.population = new double[velPop][neuralNetwork.getWeightsCount()];
        this.errors = new double[velPop];
    }

    public void run(){

        double lowestError = -1;
        double[] bestSolution = new double[neuralNetwork.getWeightsCount()];

        for (int i = 0; i < population.length; i++) {
            for (int j = 0; j < population[0].length; j++) {
                population[i][j] = rand.nextDouble() * (upperLimit - lowerLimit) + lowerLimit;
            }
            errors[i] = neuralNetwork.calculateError(population[i]);
            if (errors[i] < lowestError || lowestError < 0){
                lowestError = errors[i];
                System.arraycopy(population[i], 0, bestSolution, 0, population[i].length);
            }
        }

        double[][] testVectors = new double[population.length][population[0].length];

        int iterCounter = 0;
        int r0, r1, r2;
        int dimensions = population[0].length;
        while (iterCounter < maxIter && lowestError > errorThreshold){
            for (int i = 0; i < velPop; i++) {

                do{r0 = rand.nextInt(velPop);} while(r0 == i);
                do{r1 = rand.nextInt(velPop);} while(r1 == i || r1 == r0);
                do{r2 = rand.nextInt(velPop);} while(r2 == i || r2 == r0 || r2 == r1);

                for (int j = 0; j < dimensions; j++) {
                    testVectors[i][j] = population[i][j] + scalarF * (bestSolution[j] - population[i][j])
                            + scalarF * (population[r1][j] - population[r2][j]);
                }

                int randStart = rand.nextInt(dimensions);
                for (int j = 0; j < dimensions; j++) {
                    if (j == randStart){
                        while (rand.nextDouble() < probability) {
                            testVectors[i][j] = population[i][j];
                            j++;
                            if (j >= dimensions) break;
                        }
                    }
                }
            }
            for (int i = 0; i < velPop; i++) {
                double testError = neuralNetwork.calculateError(testVectors[i]);
                if (testError <= errors[i]){
                    errors[i] = testError;
                    System.arraycopy(testVectors[i], 0, population[i], 0, dimensions);
                    if (testError < lowestError){
                        lowestError = testError;
                        System.arraycopy(testVectors[i], 0, bestSolution, 0, testVectors[i].length);
                    }
                }
            }
            System.out.println("Iteration: " + iterCounter + " Lowest error:" + lowestError);
            iterCounter++;
        }
    }
}
