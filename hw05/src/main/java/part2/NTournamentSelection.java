package part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NTournamentSelection {

    public static Chromosome selectFittest(List<Chromosome> population, int n){
        Collections.shuffle(population);
        ArrayList<Chromosome> contenders = new ArrayList<>(population.subList(0, n));
        Collections.sort(contenders);
        return contenders.get(0);
    }

}
