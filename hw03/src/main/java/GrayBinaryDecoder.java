public class GrayBinaryDecoder extends BitVectorDecoder {


    public GrayBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public GrayBinaryDecoder(double mins, double maxs, int bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public double[] decode(BitVectorSolution solution) {

        BitVectorSolution binary = new BitVectorSolution(totalBits);
        for (int i = 0; i < totalBits; i++) {
            binary.bits[i] = solution.bits[i];
            i++;
            for (int j = 0; j < this.bits[0] - 1; j++) {
                boolean temp = Boolean.logicalXor(solution.bits[i], solution.bits[i - 1]);
                binary.bits[i] = temp;
                i++;
            }
            i--;
        }
        NaturalBinaryDecoder decoder = new NaturalBinaryDecoder(mins, maxs, bits, n);
        return decoder.decode(binary);
    }

    @Override
    public void decode(BitVectorSolution solution, double[] vector) {
        vector = decode(solution);
    }
}
