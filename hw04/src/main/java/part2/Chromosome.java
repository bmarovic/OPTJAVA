package part2;

import java.util.*;

public class Chromosome implements Comparable<Chromosome> {

    private ArrayList<Container> containers = new ArrayList<>();
    private double fitness;
    private int numberOfSticks = 0;


    public Chromosome(ArrayList<Stick> sticks, Random rand) {
        numberOfSticks = sticks.size();
        Collections.shuffle(sticks);
        ArrayList<Container> tempContainers = new ArrayList<>();

        for (int i = 0; i < sticks.size(); i++) {
            tempContainers.add(new Container());
        }

        for (Stick stick : sticks) {
            boolean added = false;
            while (!added){
                int randomContainerIndex = rand.nextInt(numberOfSticks - 1);
                if(tempContainers.get(randomContainerIndex).addStick(stick)) added = true;
            }
        }
        for (Container container : tempContainers) {
            if(container.getHeight() != 0) containers.add(container);
        }
        this.fitness = Function.valueAt(this);
    }

    public Chromosome(ArrayList<Container> containers) {
        this.containers = containers;
        for (Container cont : containers) {
            numberOfSticks += cont.getSticks().size();
        }
        this.fitness = Function.valueAt(this);
    }

    public static void addUnassignedSticks(ArrayList<Stick> unassignedSticks, ArrayList<Container> containers) {

        itemReplacement(unassignedSticks, containers);

        unassignedSticks.sort(Comparator.comparingInt(Stick::getLength));
        for (Stick unassignedStick : unassignedSticks) {
            boolean addedStick = false;
            for (Container container : containers) {
                if (container.addStick(unassignedStick)) {
                    addedStick = true;
                    break;
                }
            }
            if (!addedStick) {
                Container container = new Container();
                container.addStick(unassignedStick);
                containers.add(container);
            }
        }
    }

    private static void itemReplacement(ArrayList<Stick> unassignedSticks, ArrayList<Container> containers){

        Random rand = new Random();
        final int NUMBER_OF_TRIES = 50;

        Iterator<Stick> it = unassignedSticks.iterator();
        while (it.hasNext()) {

            Stick stick = it.next();
            for (int i = 0; i < NUMBER_OF_TRIES; i++) {
                int randomNum = rand.nextInt(containers.size() - 1);
                Container randContainer = containers.get(randomNum);
                if (stick.getLength() > randContainer.getHeight()
                        && Container.getMaxHeight() - randContainer.getHeight()
                        + stick.getLength() <= randContainer.getEmptySpace()){

                    unassignedSticks.addAll(randContainer.getSticks());
                    containers.remove(randContainer);
                    Container newContainer = new Container();
                    newContainer.addStick(stick);
                    containers.add(newContainer);
                    it.remove();
                    break;
                }
            }
        }
    }

    @Override
    public int compareTo(Chromosome solution){
        return Double.compare(this.fitness, solution.fitness);
    }

    public double getFitness() {
        return fitness;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (Container container : containers) {
            sb.append("Height: ").append(container.getHeight()).append(" ").append(container.getSticks()).append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Container> getContainers() {
        return containers;
    }

}
