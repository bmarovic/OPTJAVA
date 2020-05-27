public class Clause {

    private int[] indexes;

    public Clause(int[] indexes) {
        this.indexes = indexes;
    }

    // vraća broj literala koji čine klauzulu
    public int getSize() {
        return indexes.length;
    }

    // vraća indeks varijable koja je index-ti član ove klauzule
    public int getLiteral(int index) {
        return indexes[index];
    }

    // vraća true ako predana dodjela zadovoljava ovu klauzulu
    public boolean isSatisfied(BitVector assignment) {
        for (int index : indexes) {
            boolean bit = assignment.get(Math.abs(index));
            if ((index < 0 && !bit) || (index > 0 && bit)) return true;
        }
        return false;
    }

    @Override
    public String toString() {

        StringBuilder printableClause = new StringBuilder("");
        for (int i = 0; i < indexes.length - 1; i++){
            printableClause.append(Integer.toString(indexes[i])).append(" ");
        }
        printableClause.append(Integer.toString(indexes[indexes.length - 1]));
        return printableClause.toString();
    }

}
