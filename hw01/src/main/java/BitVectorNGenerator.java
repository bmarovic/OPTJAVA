import java.util.Iterator;

public class BitVectorNGenerator implements Iterable<MutableBitVector> {

    private MutableBitVector mutVector;
    private int numberOfVariables;

    BitVectorNGenerator(BitVector assignment) {
        this.mutVector = assignment.copy();
        numberOfVariables = assignment.vector.length;
    }

    // Vraća iterator koji na svaki next() računa sljedećeg susjeda
    @Override
    public Iterator<MutableBitVector> iterator() {
        return new Iterator<MutableBitVector>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < numberOfVariables;
            }

            @Override
            public MutableBitVector next() {
                if (!hasNext()) return null;

                MutableBitVector neighbor = mutVector.copy();
                neighbor.set(index, !mutVector.get(index));
                index++;
                return neighbor;

            }
        };

    }

    // Vraća kompletno susjedstvo kao jedno polje
    public MutableBitVector[] createNeighborhood() {
        MutableBitVector[] neighborhoodVectors = new MutableBitVector[numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            MutableBitVector neighbor = mutVector.copy();
            boolean state = mutVector.get(i + 1);
            state = !state;
            neighbor.set(i + 1, state);
            neighborhoodVectors[i] = neighbor;
        }
        return neighborhoodVectors;


    }
}
