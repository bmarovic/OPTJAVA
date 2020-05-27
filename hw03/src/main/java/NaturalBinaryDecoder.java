public class NaturalBinaryDecoder extends BitVectorDecoder{

    public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
        super(mins, maxs, bits, n);
    }

    public NaturalBinaryDecoder(double mins, double maxs, int bits, int n) {
        super(mins, maxs, bits, n);
    }

    @Override
    public double[] decode(BitVectorSolution solution) {

        int bitsPerVariable = this.bits[0];
        double[] decodedSolution = new double[totalBits / bitsPerVariable];
        double valueOfK;
        int counter = 0;


        for (int i = 0; i < totalBits;) {
            valueOfK = 0;
            for (int j = 0; j < bitsPerVariable; j++) {
                int currentBit = 0;
                if (solution.bits[i]) currentBit = 1;
                valueOfK += currentBit * Math.pow(2, (bitsPerVariable - 1) - j);
                i++;
            }
            decodedSolution[counter] = mins[counter] + valueOfK / (Math.pow(2, bitsPerVariable) - 1) * (maxs[counter] - mins[counter]);
            counter++;
        }
        return decodedSolution;
    }

    @Override
    public void decode(BitVectorSolution solution, double[] vector) {
        vector = decode(solution);
    }
}
