import java.util.Random;

public class BitVectorSolution extends SingleObjectiveSolution {

    boolean[] bits;

    public BitVectorSolution(int numberOfBits) {
        this.bits = new boolean[numberOfBits];
    }

    public BitVectorSolution newLikeThis(){
        return new BitVectorSolution(bits.length);
    }

    public BitVectorSolution duplicate(){

        BitVectorSolution copy = new BitVectorSolution(bits.length);
        System.arraycopy(this.bits, 0, copy.bits, 0, bits.length);
        return copy;
    }

    public void randomize(Random rand){

        for (int i = 0; i < bits.length; i++) {
            bits[i] = rand.nextBoolean();
        };
    }

}
