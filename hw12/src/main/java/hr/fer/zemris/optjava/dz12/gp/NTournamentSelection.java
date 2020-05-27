package hr.fer.zemris.optjava.dz12.gp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NTournamentSelection {

    public static Chromosome selectFittest(List<Chromosome> population, int n){
        Collections.shuffle(population);
        List<Chromosome> contenders = new ArrayList<>(population.subList(0, n));
        contenders.sort(Comparator.reverseOrder());
        return contenders.get(0);
    }
}
