import java.util.Random;

public class BitVectorNeighborhood implements INeighborhood<BitVectorSolution> {
    private Random rand = new Random();
    @Override
    public BitVectorSolution randomNeighbor(BitVectorSolution solution) {
        BitVectorSolution randomizedSolution = solution.duplicate();
        int randomIndex = rand.nextInt(solution.bits.length);
        randomizedSolution.bits[randomIndex] = !randomizedSolution.bits[randomIndex];
        randomIndex = rand.nextInt(solution.bits.length);
        randomizedSolution.bits[randomIndex] = !randomizedSolution.bits[randomIndex];
        return randomizedSolution;
    }

}
