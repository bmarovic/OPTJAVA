import Jama.Matrix;

import java.util.ArrayList;
import java.util.Random;

public class NumOptAlgorithms {

    private static Random rand = new Random();
    private static int minLimit = -5;
    private static int maxLimit = 5;
    private static double precision = 1e-03;
    private static double lambdaPrecision = 1e-05;
    private static int numberOfIterations = 0;
    private static ArrayList<Matrix> dataset = new ArrayList<>();


    public static Matrix gradientDescend(IFunction function, int maxIterations, Matrix startingVector){


        if (startingVector == null){
            startingVector = randomVector(function.numberOfVariables());
        }
        dataset.add(startingVector);

        for (int i = 0; i < maxIterations; i++) {


            Matrix dVector = function.valueOfGradient(startingVector).times(-1);
            if (isOptimum(dVector)) {
                System.out.println("Number of iterations: " + numberOfIterations);
                return startingVector;
            }


            double lambda = optimalLambda(startingVector, dVector, function);

            startingVector = startingVector.plus(dVector.times(lambda));
            printMatrix(startingVector);
            dataset.add(startingVector);

            numberOfIterations++;
        }

        return startingVector;
    }

    public static Matrix newtonMethod(IHFunction function, int maxIterations, Matrix startingVector){

        if (startingVector == null){
            startingVector = randomVector(function.numberOfVariables());
        }
        printMatrix(startingVector);

        dataset.add(startingVector);

        for (int i = 0; i < maxIterations; i++) {

            Matrix gradient = function.valueOfGradient(startingVector);
            if (isOptimum(gradient)) {
                System.out.println("Number of iterations: " + numberOfIterations);
                return startingVector;
            }

            Matrix dVector = function.hesseMatrix(startingVector).inverse().times(gradient).times(-1);

            double lambda = optimalLambda(startingVector, dVector, function);
            startingVector = startingVector.plus(dVector.times(lambda));
            printMatrix(startingVector);
            numberOfIterations++;
            dataset.add(startingVector);
        }
        return startingVector;
    }

    public static boolean isOptimum(Matrix gradient){

        for (int j = 0; j < gradient.getRowDimension(); j++) {
            if (Math.abs(gradient.get(j, 0)) > precision) {
                return false;
            }
        }return true;
    }

    public static Matrix randomVector(int numberOfVariables){

        Matrix startingVector  = new Matrix(numberOfVariables, 1);
        for (int i = 0; i < startingVector.getRowDimension(); i++) {
            double randDouble = minLimit + maxLimit * rand.nextDouble();
            startingVector.set(i, 0, randDouble);
        }
        return startingVector;
    }

    public static double optimalLambda(Matrix startingVector, Matrix dVector, IFunction function){

        double lambda = 0.01;
        double lambdaMin = 0;
        double lambdaMax;
        int iterationLimit = 500;
        int counter = 0;
        Matrix newVector = startingVector.plus(dVector.times(lambda));
        double thetaDerivativeOfLambda = function.valueOfGradient(newVector).transpose()
                .times(dVector).get(0,0);
        while (thetaDerivativeOfLambda < 0){

            lambda *= 2;
            newVector = startingVector.plus(dVector.times(lambda));
            thetaDerivativeOfLambda = function.valueOfGradient(newVector)
                    .transpose().times(dVector).get(0,0);

        }

        lambdaMax = lambda;
        lambda = (lambdaMin + lambdaMax)/2;
        newVector = startingVector.plus(dVector.times(lambda));
        thetaDerivativeOfLambda = function.valueOfGradient(newVector)
                .transpose().times(dVector).get(0,0);

        while(Math.abs(thetaDerivativeOfLambda - 0) > lambdaPrecision && counter < iterationLimit){
            if (thetaDerivativeOfLambda > 0) lambdaMax = lambda;
            else lambdaMin = lambda;
            lambda = (lambdaMin + lambdaMax) / 2;
            newVector = startingVector.plus(dVector.times(lambda));
            thetaDerivativeOfLambda = function.valueOfGradient(newVector)
                    .transpose().times(dVector).get(0,0);
            counter++;
        }
        return lambda;
    }

    public static void printMatrix(Matrix matrix){

        for (int i = 0; i < matrix.getRowDimension(); i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < matrix.getColumnDimension(); j++) {

                row.append(matrix.get(i, j)).append(" ");

            }
            System.out.println(row.toString());
        }
        System.out.println();
    }


    public static ArrayList<Matrix> getDataset() {
        return dataset;
    }
}
