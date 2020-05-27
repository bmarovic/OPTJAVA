package part2;

import java.util.ArrayList;

public class CostFunction implements IFunction{

    private int[][] distanceMatrix;
    private int[][] contentMatrix;

    public CostFunction(int[][] distanceMatrix, int[][] contentMatrix) {
        this.distanceMatrix = distanceMatrix;
        this.contentMatrix = contentMatrix;
    }

    @Override
    public double valueAt(Chromosome chromosome) {

        ArrayList<Integer> vector = chromosome.getFactoryDistribution();

        double cost = 0;
        for (int i = 0; i < distanceMatrix.length; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                cost += contentMatrix[i][j] * distanceMatrix[vector.get(i)][vector.get(j)];
            }
        }
        return cost;
    }
}
