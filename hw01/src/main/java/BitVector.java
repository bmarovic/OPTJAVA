import java.util.Random;

public class BitVector {

    boolean[] vector;

    public BitVector(Random rand, int numberOfBits){
        vector = new boolean[numberOfBits];
        for (int i = 0; i < numberOfBits ; i++) {
            vector[i] = rand.nextBoolean();
        }
    }

    public BitVector(boolean ... bits) {
        this.vector = bits;
    }

    public BitVector(int n){
        this.vector = new boolean[n];
    }

    public boolean get(int index){
        return this.vector[index - 1];
    }

    @Override
    public String toString() {
        StringBuilder printableVector = new StringBuilder();
        for (boolean bit : vector) {
            if (bit) printableVector.append("1");
            else printableVector.append("0");
        }
        return printableVector.toString();
    }

    public MutableBitVector copy(){
        return new MutableBitVector(this.vector.clone());
    }

}