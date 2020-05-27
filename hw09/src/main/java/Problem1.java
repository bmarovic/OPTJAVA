public class Problem1 implements MOOPProblem {

    private static final int numberOfObjectives = 4;

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void evaluateSolution(double[] solution, double[] objectives) {
        for (int i = 0; i < solution.length; i++) {
            objectives[i] = Math.pow(solution[i], 2);
        }
    }


}
