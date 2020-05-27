package part1;

import java.util.Random;

public class Crossover {

    public static final Random rand = new Random();

    public static Chromosome cross(Chromosome parent1, Chromosome parent2){

        Chromosome child = new Chromosome(parent1.numberOfBits());
        boolean[] childBits = child.getBits();

        for (int i = 0; i < parent1.numberOfBits(); i++) {
            if(rand.nextDouble() > 0.5){
                childBits[i] = parent1.getBits()[i];
            }else {
                childBits[i] = parent2.getBits()[i];
            }
        }

        return child;
    }

}
