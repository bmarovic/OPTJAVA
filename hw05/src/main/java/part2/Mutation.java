package part2;


import java.util.ArrayList;
import java.util.Random;

public class Mutation {

    private static final double MUTATION_PROBABILITY = 0.08;
    private static final Random rand = new Random();

    public static void mutate(Chromosome chromosome){

        if (rand.nextDouble() < MUTATION_PROBABILITY){
            ArrayList<Integer> factories = chromosome.getFactoryDistribution();
            int randomIndex1 = rand.nextInt(factories.size() - 1);
            int randomIndex2 = rand.nextInt(factories.size() - 1);
            while (randomIndex1 == randomIndex2){
                randomIndex2 = rand.nextInt(factories.size() - 1);
            }
            int temp = factories.get(randomIndex1);
            factories.set(randomIndex1, factories.get(randomIndex2));
            factories.set(randomIndex2, temp);


        }
    }


}
