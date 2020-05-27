public interface MOOPProblem {
    int getNumberOfObjectives();
    void evaluateSolution(double[] solution, double[] objectives);
}
