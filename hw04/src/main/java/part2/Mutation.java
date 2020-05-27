package part2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Mutation {

    private static final Random rand = new Random();

    public static void mutate(Chromosome child, double mutationProb){

        ArrayList<Stick> unassignedSticks = new ArrayList<>();
        ArrayList<Container> containers = new ArrayList<>();
        for (Container container : child.getContainers()) {
            containers.add(container.duplicate());
        }
        Iterator<Container> iterator = containers.iterator();
        while (iterator.hasNext()){
            Container container = iterator.next();
            if(rand.nextDouble() < mutationProb){
                unassignedSticks.addAll(container.getSticks());
                iterator.remove();
            }
        }
        Chromosome.addUnassignedSticks(unassignedSticks, containers);
    }

}
