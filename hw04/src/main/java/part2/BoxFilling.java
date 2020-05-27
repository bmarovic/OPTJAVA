package part2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class BoxFilling {

    private static double MUTATION_PROB;
    private static int MAX_HEIGHT;
    private static int MIN_LENGTH;
    private static int VEL_POP;
    private static String path;
    private static int N;
    private static int M;
    private static boolean P;
    private static int MAX_ITER;
    private static final Random rand = new Random();

    public static void main(String[] args) {

        //parameters are in order: path, VEL_POP, N, M, P, MAX_ITER, mutation chance

        ArrayList<Integer> sizes = null;

        try {
            if(args.length != 7){
                System.out.println("Invalid number of arguments, number has to be 7.");
            }
            path = args[0];
            String[] fileParams = path.split("-");
            MAX_HEIGHT = Integer.parseInt(fileParams[1]);
            MIN_LENGTH = Integer.parseInt(fileParams[2]);
            VEL_POP = Integer.parseInt(args[1]);
            N = Integer.parseInt(args[2]);
            if (N < 2){
                System.out.println("N has to be >= 2.");
                System.exit(1);
            }
            M = Integer.parseInt(args[3]);
            if (M < 2){
                System.out.println("M has to be >= 2.");
                System.exit(1);
            }
            P = Boolean.parseBoolean(args[4]);
            MAX_ITER = Integer.parseInt(args[5]);
            MUTATION_PROB = Double.parseDouble(args[6]);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(BoxFilling.class.getClassLoader()
                                            .getResourceAsStream(path))),
                            "UTF-8"));

            String line = br.readLine();
            String[] stringValues = line.replace("[","")
                                        .replace("]", "")
                                        .replace(" ", "")
                                        .split(",");

            sizes = new ArrayList<>();
            for (String stringValue : stringValues) {
                sizes.add(Integer.parseInt(stringValue));
            }


        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<Stick> sticks = new ArrayList<>();

        for (int i = 0; i < sizes.size(); i++) {
            sticks.add(new Stick(sizes.get(i), i));
        }

        ArrayList<Chromosome> population = new ArrayList<>();
        Container.setMaxHeight(MAX_HEIGHT);

        for (int i = 0; i < VEL_POP; i++) {
            population.add(new Chromosome(sticks, rand));
        }
        Collections.sort(population);
        Chromosome fittestChromosome = population.get(0);

        for (int i = 0; i < MAX_ITER; i++) {

            Chromosome parent1 = NTournamentSelection.select(population, N, true);
            Chromosome parent2 = NTournamentSelection.select(population, N, true);
            Chromosome child = Crossover.crossover(parent1, parent2);
            Mutation.mutate(child, MUTATION_PROB);

            Chromosome replaceable = NTournamentSelection.select(population, M, false);
            if (P){
                if (child.compareTo(replaceable) < 0){
                    population.set(population.indexOf(replaceable), child);
                    if (child.compareTo(fittestChromosome) < 0){
                        fittestChromosome = child;
                    }
                }
            }else{
                population.set(population.indexOf(replaceable), child);
                if (child.compareTo(fittestChromosome) < 0){
                    fittestChromosome = child;
                }
            }

            System.out.println("Fitness = " + fittestChromosome.getFitness());

            if (fittestChromosome.getFitness() == MIN_LENGTH){
                System.out.println(fittestChromosome);
                System.out.println("Iterations: " + i);
                break;
            }

        }


    }

}
