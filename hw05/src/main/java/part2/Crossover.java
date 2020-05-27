package part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Crossover {

    private static Random rand = new Random();

    public static Chromosome cross(Chromosome parent1, Chromosome parent2){

        ArrayList<Integer> parent1Factories = parent1.getFactoryDistribution();
        ArrayList<Integer> parent2Factories = parent2.getFactoryDistribution();

        int breakPoint1A = rand.nextInt(parent1Factories.size() - 1);
        int breakPoint2A = rand.nextInt(parent1Factories.size() - 1);

        if (breakPoint1A > breakPoint2A) {
            int temp = breakPoint1A;
            breakPoint1A = breakPoint2A;
            breakPoint2A = temp;
        }

        ArrayList<Integer> duplicates = new ArrayList<>();
        ArrayList<Integer> childFactories = new ArrayList<>();
        ArrayList<Integer> lossPrevention = new ArrayList<>();

        for (int i = 0; i < parent1Factories.size(); i++) {
            childFactories.add(null);
        }

        for (int i = breakPoint1A; i < breakPoint2A; i++) {
            duplicates.add(parent1.getFactoryDistribution().get(i));
            childFactories.set(i, parent1.getFactoryDistribution().get(i));
        }


        for (int i = 0; i < parent2.getFactoryDistribution().size(); i++) {
            if(childFactories.get(i) == null && !duplicates.contains(parent2Factories.get(i))){
                    childFactories.set(i, parent2Factories.get(i));
            }else if(!duplicates.contains(parent2Factories.get(i))){
                    lossPrevention.add(parent2Factories.get(i));
                }
        }

        Collections.shuffle(lossPrevention);

        for (Integer factory : lossPrevention) {
            for (int i = 0; i < parent1Factories.size(); i++) {
                if(childFactories.get(i) == null){
                    childFactories.set(i, factory);
                    break;
                }
            }
        }

        return new Chromosome(childFactories);
    }


}
