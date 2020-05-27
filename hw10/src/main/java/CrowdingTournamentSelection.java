import java.util.List;
import java.util.Random;

public class CrowdingTournamentSelection {

    public static Chromosome[] select(List<Chromosome> population) {
        Chromosome parent1 = getParent(population);
        Chromosome parent2 = getParent(population);
        while (parent1 == parent2) {
            parent2 = getParent(population);
        }
        return new Chromosome[]{parent1, parent2};
    }

    private static Chromosome getParent(List<Chromosome> population) {
        Random rand = new Random();
        Chromosome chromosome1 = population.get(rand.nextInt(population.size()));
        Chromosome chromosome2 = population.get(rand.nextInt(population.size()));
        if (chromosome1.compareTo(chromosome2) > 0) return chromosome1;
        return chromosome2;
    }
}
