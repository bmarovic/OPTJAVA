import java.util.Random;

public class BLXAlphaCrossover {

    private static final double alpha = 0.5;
    private static Random rand = new Random();

    public static Chromosome crossover(Chromosome parent1, Chromosome parent2){

        int size = parent1.numberOfVariables();

        double[] cMin = new double[size];
        double[] cMax = new double[size];
        double[] IValues = new double[size];

        for (int i = 0; i < size; i++) {
            double c1 = parent1.getAtIndex(i);
            double c2 = parent2.getAtIndex(i);
            if(c1 > c2){
                cMin[i] = c2;
                cMax[i] = c1;
                IValues[i] = c1 - c2;
            }else{
                cMin[i] = c1;
                cMax[i] = c2;
                IValues[i] = c2 - c1;
            }
        }

        Chromosome child = new Chromosome(size);
        for (int i = 0; i < size; i++) {
            double rangeMin = cMin[i] - IValues[i] * alpha;
            double rangeMax = cMax[i] + IValues[i] * alpha;
            double randomValue = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
            child.setAtIndex(randomValue, i);
        }
        return child;
    }

}
