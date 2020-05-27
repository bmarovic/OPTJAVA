import java.util.Random;

public abstract class PSOAlgorithm{

    int velPop;
    int maxIter;
    double errorThreshold;
    FFANN ffann;
    Random rand = new Random();

    static double X_MAX = 1.0;
    static double X_MIN = - 1.0;
    static double V_MIN = - 0.3;
    static double V_MAX = 0.3;
    static double PHI_VALUE = 2.05;
    static double INERTIA_MAX = 0.9;
    static double INERTIA_MIN = 0.2;

    static double[][] positions;
    static double[] positionError;
    static double[][] velocities;
    static double[][] personalBest;
    static double[] personalBestError;
    static double[] globalBest;
    static double globalBestError;
    static int dimension;

    PSOAlgorithm(int velPop, int maxIter, double errorThreshold, FFANN ffann) {
        this.velPop = velPop;
        this.maxIter = maxIter;
        this.errorThreshold = errorThreshold;
        this.ffann = ffann;

        dimension = ffann.getWeightsCount();
        positions = new double[velPop][dimension];
        positionError = new double[velPop];
        velocities = new double[velPop][dimension];
        personalBest = new double[velPop][dimension];
        personalBestError = new double[velPop];
        globalBest = new double[dimension];
        globalBestError = 0;
    }

    public abstract void run();

    void initializePopulation() {
        for (int i = 0; i < velPop; i++) {
            for (int j = 0; j < dimension; j++) {
                double randPosition = X_MIN + (X_MAX - X_MIN) * rand.nextDouble();
                positions[i][j] = randPosition;
                personalBest[i][j] = randPosition;
                velocities[i][j] = V_MIN + (V_MAX - V_MIN) * rand.nextDouble();
            }
            positionError[i] = ffann.calculateError(positions[i]);
            personalBestError[i] = positionError[i];
        }
    }

    void evaluationGlobal() {
        for (int i = 0; i < velPop; i++) {
            positionError[i] = ffann.calculateError(positions[i]);
            if (globalBestError == 0){
                globalBestError = positionError[i];
            }else if(globalBestError > positionError[i]){
                globalBestError = positionError[i];
                System.arraycopy(positions[i], 0, globalBest, 0, dimension);
            }
            if (personalBestError[i] > positionError[i]) {
                System.arraycopy(positions[i], 0, personalBest[i], 0, dimension);
                personalBestError[i] = positionError[i];
            }
        }
    }

    void checkRange(int i, int j){
        if (velocities[i][j] > V_MAX){
            velocities[i][j] = V_MAX;
            return;
        }
        if (velocities[i][j] < V_MIN){
            velocities[i][j] = V_MIN;
        }
    }
}
