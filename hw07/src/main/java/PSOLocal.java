public class PSOLocal extends PSOAlgorithm {

    private int neighborhoodHalfWidth;
    private double[][] localBest;
    private double[] localBestError;

    public PSOLocal(int VEL_POP, int MAX_ITER, double ERROR_THRESHOLD, FFANN ffann, int neighborhoodHalfWidth) {
        super(VEL_POP, MAX_ITER, ERROR_THRESHOLD, ffann);
        this.neighborhoodHalfWidth = neighborhoodHalfWidth;
        localBest = new double[velPop][dimension];
        localBestError = new double[velPop];
    }

    public void run() {

        initializePopulation();
        evaluationGlobal();
        for (int i = 0; i < personalBest.length; i++) {
            System.arraycopy(personalBest[i], 0, localBest[i], 0, dimension);
            localBestError[i] = positionError[i];
        }

        double iterCounter = 0;
        while (iterCounter < maxIter && globalBestError > errorThreshold){
            evaluationGlobal();
            evaluationLocal();
            double weight = (maxIter - iterCounter++)/(maxIter) * (INERTIA_MAX - INERTIA_MIN) + INERTIA_MIN;

            for (int i = 0; i < velPop; i++) {
                for (int j = 0; j < dimension; j++) {
                    velocities[i][j] = weight * velocities[i][j] + PHI_VALUE * rand.nextDouble() *
                            (personalBest[i][j] - positions[i][j]) + PHI_VALUE * rand.nextDouble() *
                            (localBest[i][j] - positions[i][j]);

                    checkRange(i, j);
                    positions[i][j] = positions[i][j] + velocities[i][j];
                }
            }
        }
        System.out.println("Minimal error: " + globalBestError);
        ffann.printStatistics(globalBest);
    }

    private void evaluationLocal(){
        for (int i = 0; i < velPop; i++) {
            for (int j = i - neighborhoodHalfWidth; j < i + neighborhoodHalfWidth; j++) {
                int neighborIndex = j % velPop;
                if(neighborIndex < 0) neighborIndex = velPop + neighborIndex;
                if(neighborIndex > velPop) neighborIndex -= velPop;
                if(personalBestError[neighborIndex] < localBestError[i]){
                    localBestError[i] = personalBestError[neighborIndex];
                    System.arraycopy(personalBest[neighborIndex], 0, localBest[i], 0, dimension);
                }
            }
        }
    }
}
