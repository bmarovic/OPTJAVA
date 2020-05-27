package hr.fer.zemris.generic.ga;

import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.RNG;

public class OneBreakpointCrossover implements ICrossover{

    private IRNG rand = RNG.getRNG();

    public Chromosome crossover(Chromosome parent1, Chromosome parent2){
        int numberOfVariables = parent1.numberOfVariables();
        Chromosome child = new Chromosome(new int[numberOfVariables]);
        int breakpoint = rand.nextInt(0, numberOfVariables);
        for (int i = 0; i < numberOfVariables; i++) {
            if (i <= breakpoint){
                child.setAtIndex(parent1.getAtIndex(i), i);
            } else {
                child.setAtIndex(parent2.getAtIndex(i), i);
            }
        }
        return child;
    }

}
