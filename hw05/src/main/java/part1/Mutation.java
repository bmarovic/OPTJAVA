package part1;

import java.util.Random;

public class Mutation {

    private static final double MUTATION_PROBABILITY = 0.02;
    private static final Random rand = new Random();

    public static void mutate(Chromosome chromosome){
        boolean[] bits = chromosome.getBits();
        for (int i = 0; i < bits.length; i++) {
            if(rand.nextDouble() < MUTATION_PROBABILITY){
                bits[i] = !bits[i];
            }
        }
    }

}
