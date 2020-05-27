package part1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class GeneticAlgorithm {

    //500 1e-2 1000 tournament:10 0.01
    //400 1e-2 1000 rouletteWheel 0.01
    private static final int NUMBER_OF_VARIABLES = 6;
    private static int VEL_POP;
    private static double SIGMA;
    private static double PRECISION;
    private static int MAX_GEN;
    private static String SELECTION;
    private static double alpha = 0.5;
    private final static double RANGE_MIN = -8;
    private final static double RANGE_MAX = 8;
    private static Random rand = new Random();


    public static void main(String[] args) {

        double[][] xValues = new double[20][5];
        double[] yValues = new double[20];

        try {
            VEL_POP = Integer.parseInt(args[0]);
            PRECISION = Double.parseDouble(args[1]);
            MAX_GEN = Integer.parseInt(args[2]);
            SELECTION = args[3];
            SIGMA = Double.parseDouble(args[4]);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                    new BufferedInputStream(
                            Objects.requireNonNull(GeneticAlgorithm.class.getClassLoader()
                                    .getResourceAsStream("02-zad-prijenosna.txt"))),
                            "UTF-8"));

            String line = br.readLine();
            while (line.startsWith("#")) {
                line = br.readLine();
            }

            for (int i = 0; i < 20; i++) {

                String[] values = line.replace("[","")
                        .replace("]","")
                        .replace(",","")
                        .replace("   "," ")
                        .replace("  ", " ")
                        .trim()
                        .split(" ");
                int j = 0;
                while (j < 5){
                    xValues[i][j] = Double.parseDouble(values[j]);
                    j++;
                }
                yValues[i] = Double.parseDouble(values[j]);
                line = br.readLine();
            }


        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<Chromosome> population = new ArrayList<>();
        IFunction function = new FunctionTransfer(xValues, yValues);
        double[] mins = new double[NUMBER_OF_VARIABLES];
        double[] maxs = new double[NUMBER_OF_VARIABLES];
        Arrays.fill(mins, RANGE_MIN);
        Arrays.fill(maxs, RANGE_MAX);


        for (int i = 0; i < VEL_POP; i++) {
            Chromosome solution = new Chromosome(NUMBER_OF_VARIABLES);
            solution.randomize(rand, mins, maxs);
            solution.fitness = function.valueAt(solution.values);
            population.add(solution);
        }

        Chromosome parent1 = null;
        Chromosome parent2 = null;
        BLXAlphaCrossover blxAlphaCrossover = new BLXAlphaCrossover(alpha, rand);

        for (int i = 0; i < MAX_GEN; i++) {
            Collections.sort(population);
            ArrayList<Chromosome> newPopulation = new ArrayList<>(population.subList(0, 1));
            while(newPopulation.size() < VEL_POP){
                if (SELECTION.equals("rouletteWheel")){
                    parent1 = RouletteWheelSelection.selectFittest(population);
                    parent2 = RouletteWheelSelection.selectFittest(population);
                }else if (SELECTION.startsWith("tournament:")){
                    int n = Integer.parseInt(SELECTION.substring("tournament:".length()));
                    parent1 = NTournamentSelection.selectFittest(population, n);
                    parent2 = NTournamentSelection.selectFittest(population, n);
                }else{
                    System.err.println("Wrong declaration of the selection method");
                    System.exit(1);
                }

                Chromosome child = blxAlphaCrossover.crossover(parent1, parent2);
                child = Mutation.mutate(child, SIGMA);
                child.fitness = function.valueAt(child.values);
                newPopulation.add(child);
            }
            System.out.println(newPopulation.get(0));
            if (newPopulation.get(0).fitness < PRECISION){
                System.exit(0);
            }
            population = newPopulation;
        }
    }

}
