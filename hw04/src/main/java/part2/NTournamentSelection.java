package part2;

import java.util.ArrayList;
import java.util.Collections;

public class NTournamentSelection {

    public static Chromosome select(ArrayList<Chromosome> population, int n, boolean fittest){
        Collections.shuffle(population);
        ArrayList<Chromosome> contenders = new ArrayList<>(population.subList(0, n));
        Collections.sort(contenders);
        if(fittest) return contenders.get(0);
        return contenders.get(contenders.size() - 1);
    }

}
