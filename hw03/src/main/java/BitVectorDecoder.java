public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution> {

    double[] mins;
    double[] maxs;
    int[] bits;
    int n;
    int totalBits;


    public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        this.mins = mins;
        this.maxs = maxs;
        this.bits = bits;
        this.n = n;
        for (int i = 0; i < n; i++) {
            this.totalBits += bits[i];
        }
    }

    public BitVectorDecoder(double mins, double maxs, int bits, int n) {
        this.mins = new double[n];
        this.maxs = new double[n];
        this.bits = new int[n];
        this.n = n;

        for (int i = 0; i < n; ++i){
            this.mins[i] = mins;
            this.maxs[i] = maxs;
            this.bits[i] = bits;
        }
        this.totalBits = n * bits;
    }

    public int getDimensions() {
        return n;
    }

    public int getTotalBits() {
        return totalBits;
    }

    @Override
    public abstract double[] decode(BitVectorSolution solution);

    @Override
    public abstract void decode(BitVectorSolution solution, double[] vector);
}
