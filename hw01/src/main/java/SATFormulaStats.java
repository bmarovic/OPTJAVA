public class SATFormulaStats {

    private SATFormula formula;
    static final int numberOfBest = 2;
    private static final double percentageConstantUp = 0.01;
    private static final double percentageConstantDown = 0.1;
    private static final double percentageUnitAmount = 50;
    private double[] post;
    private int numberOfSatisfied;
    private boolean formulaSatisfied;
    private double percentageBonus;

    public SATFormulaStats(SATFormula formula) {
        this.formula = formula;
        this.post = new double[formula.getNumberOfClauses()];
    }

    // analizira se predano rješenje i pamte svi relevantni pokazatelji
    public void setAssignment(BitVector assignment, boolean updatePercentages) {

        formulaSatisfied = formula.isSatisfied(assignment);
        numberOfSatisfied = 0;
        percentageBonus = 0;

        for (int i = 0; i < formula.getNumberOfClauses() ; i++) {
            boolean clauseSatisfied = formula.getClause(i).isSatisfied(assignment);
            if (clauseSatisfied){
                numberOfSatisfied++;
                if (updatePercentages){
                    post[i] += (1 - post[i]) * percentageConstantUp;
                }
                else percentageBonus += percentageUnitAmount * (1 - post[i]);

            }else{
                if (updatePercentages){
                    post[i] += (0 - post[i]) * percentageConstantDown;
                }
                else percentageBonus -= percentageUnitAmount * (1 - post[i]);
            }
        }
    }

    // vraća temeljem onoga što je setAssignment zapamtio: broj klauzula koje su zadovoljene
    public int getNumberOfSatisfied() {
        return numberOfSatisfied;
    }

    // vraća temeljem onoga što je setAssignment zapamtio
    public boolean isSatisfied() {
        return formulaSatisfied;
    }

    // vraća temeljem onoga što je setAssignment zapamtio: suma korekcija klauzula
    public double getPercentageBonus() {
        return percentageBonus;
    }

    // vraća temeljem onoga što je setAssignment zapamtio: procjena postotka za klauzulu
    public double getPercentage(int index) {
        return post[index];
    }

}
