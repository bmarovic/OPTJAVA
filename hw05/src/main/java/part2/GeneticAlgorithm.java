package part2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class GeneticAlgorithm {

    static int NUMBER_OF_SUBPOPULATIONS;
    static int NUMBER_OF_ENTITIES;
    static int VEL_POP = 2000;
    static int[][] distanceMatrix;
    static int[][] contentMatrix;

    /*
    els19.dat 5
    nug12.dat 5
    nug25.dat 30
     */

    public static void main(String[] args) {

        try {
            String path = args[0];
            NUMBER_OF_SUBPOPULATIONS = Integer.parseInt(args[1]);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(GeneticAlgorithm.class.getClassLoader()
                                            .getResourceAsStream(path))),
                            "UTF-8"));

            String line = br.readLine().trim();
            NUMBER_OF_ENTITIES = Integer.parseInt(line);
            br.readLine();

            distanceMatrix = new int[NUMBER_OF_ENTITIES][NUMBER_OF_ENTITIES];
            contentMatrix = new int[NUMBER_OF_ENTITIES][NUMBER_OF_ENTITIES];
            parseMatrix(br, distanceMatrix);
            parseMatrix(br, contentMatrix);
            br.close();

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        IFunction function = new CostFunction(distanceMatrix, contentMatrix);
        ArrayList<Chromosome> population = new ArrayList<>();

        while (population.size() != VEL_POP) {
            Chromosome chromosome = new Chromosome(NUMBER_OF_ENTITIES);
            chromosome.randomize();
            chromosome.setError(function.valueAt(chromosome));
            if(!population.contains(chromosome)) population.add(chromosome);
        }

        for (int i = NUMBER_OF_SUBPOPULATIONS; i != 0 ; i--) {
            int subPopSize = VEL_POP / i;

            for (int j = 0; j < i - 1; j++) {
                int lowerBound = j * subPopSize;
                int upperBound = (j + 1) * subPopSize;
                System.out.println("Offspring selection for N = " + i + " subpopulation: " + j);
                OffspringSelection.run(function, population.subList(lowerBound,upperBound));
            }
            System.out.println("Offspring selection for N = " + i + " subpopulation: " + i);
            ArrayList<Chromosome> subPopulation = new ArrayList<>(population.subList((i - 1) * subPopSize,
                    population.size()));
            OffspringSelection.run(function, subPopulation);
        }
        System.out.println(population.stream().min(Chromosome::compareTo).get());




    }

    private static void parseMatrix(BufferedReader br, int[][] matrix) throws IOException {
        String line = br.readLine();
        int row = 0;
        while(!line.equals("")){

            String[] rowList = line.trim()
                    .replace("     ", " ")
                    .replace("    ", " ")
                    .replace("   ", " ")
                    .replace("  ", " ")
                    .split(" ");
            for (int i = 0; i < rowList.length; i++) {
                matrix[row][i] = Integer.parseInt(rowList[i]);
            }
            row++;
            line = br.readLine();
            if (line == null) break;

        }
    }

}
