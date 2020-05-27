public class SATFormula {

    private int numberOfVariables;
    private Clause[] clauses;

    public SATFormula(int numberOfVariables, Clause[] clauses) {
        this.numberOfVariables = numberOfVariables;
        this.clauses = clauses;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public int getNumberOfClauses() {
        return clauses.length;
    }

    public Clause getClause(int index) {
        return clauses[index];
    }

    public boolean isSatisfied(BitVector assignment) {
        for (Clause clause : clauses) {
            if (!clause.isSatisfied(assignment)) return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder printableFormula = new StringBuilder("f = ");
        for (Clause clause : clauses) {
            printableFormula.append("(").append(clause.toString()).append(")");
        }
        return printableFormula.toString();
    }
}
