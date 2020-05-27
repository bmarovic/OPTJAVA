public class PSOGlobal extends PSOAlgorithm{

    public PSOGlobal(int velPop, int maxIter, double errorThreshold, FFANN ffann) {
        super(velPop, maxIter, errorThreshold, ffann);
        globalBest = new double[dimension];
        globalBestError = 0;
    }

    public void run() {
        initializePopulation();
        evaluationGlobal();
        double iterCounter = 0;
        while (iterCounter < maxIter && globalBestError > errorThreshold){
            evaluationGlobal();
            double weight = (maxIter - iterCounter++)/(maxIter) * (INERTIA_MAX - INERTIA_MIN) + INERTIA_MIN;
            for (int i = 0; i < velPop; i++) {
                for (int j = 0; j < dimension; j++) {
                    velocities[i][j] = weight * velocities[i][j] + PHI_VALUE * rand.nextDouble() *
                            (personalBest[i][j] - positions[i][j]) + PHI_VALUE * rand.nextDouble() *
                            (globalBest[j] - positions[i][j]);

                    checkRange(i, j);
                    positions[i][j] = positions[i][j] + velocities[i][j];
                }
            }
        }
        System.out.println("Minimal error: " + globalBestError);
        ffann.printStatistics(globalBest);
    }

}
