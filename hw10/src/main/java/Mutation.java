import java.util.Random;

public class Mutation {
    private static double sigma = 0.3;
    public static Chromosome mutate(Chromosome child){
        Random rand = new Random();
        for (int i = 0; i < child.numberOfVariables(); i++) {
            child.setAtIndex(child.getAtIndex(i) + rand.nextGaussian() * sigma, i);
        }
        return child;
    }

}
