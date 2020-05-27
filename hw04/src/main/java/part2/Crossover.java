package part2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class Crossover {

    private static final Random rand = new Random();

    public static Chromosome crossover(Chromosome parent1, Chromosome parent2){

        ArrayList<Container> parent1List = new ArrayList<>();
        ArrayList<Container> parent2List = new ArrayList<>();

        for (Container container : parent1.getContainers()) {
            parent1List.add(container.duplicate());
        }
        for (Container container : parent2.getContainers()) {
            parent2List.add(container.duplicate());
        }

        ArrayList<Container> childContainers = new ArrayList<>();

        int breakPoint1A = rand.nextInt(parent1List.size() - 1);
        int breakPoint2A = rand.nextInt(parent1List.size() - 1);

        if (breakPoint1A > breakPoint2A) {
            int temp = breakPoint1A;
            breakPoint1A = breakPoint2A;
            breakPoint2A = temp;
        }

        HashSet<Stick> duplicates = new HashSet<>();

        for (int i = breakPoint1A; i <= breakPoint2A; i++) {
            childContainers.add(parent1List.get(i).duplicate());
            duplicates.addAll(parent1List.get(i).getSticks());
        }

        HashSet<Stick> unassignedSticks = new HashSet<>();
        Iterator<Container> iterator = parent2List.iterator();
        while (iterator.hasNext()){
            boolean containsDuplicate = false;
            Container container = iterator.next();
            for (Stick stick : container.getSticks()) {
                if (duplicates.contains(stick)) {
                    containsDuplicate = true;
                    break;
                }
            }
            if (!containsDuplicate) childContainers.add(container.duplicate());
            else {
                for (Stick stick : container.getSticks()) {
                    if (!duplicates.contains(stick)) unassignedSticks.add(stick);
                }
                iterator.remove();
            }
        }
        Chromosome.addUnassignedSticks(new ArrayList<>(unassignedSticks), childContainers);
        return new Chromosome(childContainers);
    }

}
