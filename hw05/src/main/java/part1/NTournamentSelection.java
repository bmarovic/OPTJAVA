package part1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class NTournamentSelection {

    public static Chromosome selectFittest(ArrayList<Chromosome> population, int n){
        Collections.shuffle(population);
        ArrayList<Chromosome> contenders = new ArrayList<>(population.subList(0, n));
        contenders.sort(Comparator.reverseOrder());
        return contenders.get(0);
    }

}
