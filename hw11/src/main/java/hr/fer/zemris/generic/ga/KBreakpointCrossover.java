package hr.fer.zemris.generic.ga;

import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.RNG;

import java.util.*;

public class KBreakpointCrossover implements ICrossover {

    private IRNG rand = RNG.getRNG();
    private int numberOfBreakpoints;
    List<Integer> breakpoints = new ArrayList<>();

    public KBreakpointCrossover(int numberOfBreakpoints) {
        this.numberOfBreakpoints = numberOfBreakpoints;
    }

    public Chromosome crossover(Chromosome parent1, Chromosome parent2){
        int numberOfVariables = parent1.numberOfVariables();
        Chromosome child = new Chromosome(new int[numberOfVariables]);
        while (breakpoints.size() != numberOfBreakpoints) {
            breakpoints.add(rand.nextInt(0, numberOfVariables));
        }
        breakpoints.sort(Comparator.naturalOrder());
        boolean firstParent = true;
        int bpIndex = 0;
        for (int i = 0; i < numberOfVariables; i++) {
            if (firstParent){
                child.setAtIndex(parent1.getAtIndex(i), i);
            } else {
                child.setAtIndex(parent2.getAtIndex(i), i);
            }
            if (bpIndex < breakpoints.size() && i == breakpoints.get(bpIndex)) {
                firstParent = !firstParent;
                bpIndex++;
            }
        }
        breakpoints.clear();
        return child;
    }
}
