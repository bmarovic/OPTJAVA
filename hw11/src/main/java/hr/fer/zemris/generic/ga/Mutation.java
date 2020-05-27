package hr.fer.zemris.generic.ga;


import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.rng.IRNG;
import hr.fer.zemris.rng.RNG;

public class Mutation {

    private IRNG rand = RNG.getRNG();
    private GrayScaleImage referenceImage;

    public Mutation(GrayScaleImage referenceImage) {
        this.referenceImage = referenceImage;
    }

    public Chromosome mutate(Chromosome child){
        for (int i = 0; i < child.numberOfVariables(); i++) {
            if (rand.nextDouble() < 0.01) {
                switch (i % 5){
                    case 0:
                        child.setAtIndex(rand.nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE), i);
                        break;
                    case 1:
                        child.setAtIndex(rand.nextInt(0, referenceImage.getWidth()), i);
                        break;

                    case 2:
                        child.setAtIndex(rand.nextInt(0, referenceImage.getHeight()), i);
                        break;

                    case 3:
                        child.setAtIndex(rand.nextInt(0, referenceImage.getWidth()), i);
                        break;

                    case 4:
                        child.setAtIndex(rand.nextInt(0, referenceImage.getHeight()), i);
                        break;

                }
            }
        }
        return child;
    }

}
