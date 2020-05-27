package part1;

        import java.util.ArrayList;
        import java.util.Collections;

public class NTournamentSelection {

    public static Chromosome selectFittest(ArrayList<Chromosome> population, int n){
        Collections.shuffle(population);
        ArrayList<Chromosome> contenders = new ArrayList<>(population.subList(0, n));
        Collections.sort(contenders);
        return contenders.get(0);
    }

}
