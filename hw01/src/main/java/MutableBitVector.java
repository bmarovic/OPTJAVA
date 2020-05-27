public class MutableBitVector extends BitVector {


    public MutableBitVector(boolean ... bits){
        super(bits);
    }

    public MutableBitVector(int n){
        super(n);
    }

    public void set(int index, boolean value){
        this.vector[index - 1] = value;
    }

}
