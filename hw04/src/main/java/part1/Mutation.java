package part1;

import java.util.Random;

public class Mutation {

    public static Chromosome mutate(Chromosome child, double sigma){
        Random rand = new Random();
        for (int i = 0; i < child.numberOfVariables(); i++) {
            child.setAtIndex(child.getAtIndex(i) + rand.nextGaussian() * sigma, i);
        }
        return child;
    }

}
