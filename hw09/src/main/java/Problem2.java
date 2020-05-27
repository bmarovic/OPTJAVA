public class Problem2 implements MOOPProblem {

    private static final int numberOfObjectives = 2;

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void evaluateSolution(double[] solution, double[] objectives) {
        objectives[0] = solution[0];
        objectives[1] = (1 + solution[1]) / solution[0];
    }
}
