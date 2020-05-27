import Jama.Matrix;

public class Function2 implements IHFunction{


    @Override
    public int numberOfVariables() {
        return 2;
    }

    @Override
    public double valueOfFunction(Matrix vector, int row) {
        return Math.pow(vector.get(0, 0) - 1,2) - 10 * Math.pow(vector.get(1, 0) - 2, 2);
    }


    @Override
    public Matrix valueOfGradient(Matrix vector) {
        double[][] gradient = {{2 * vector.get(0, 0) - 2}, {20 * vector.get(1, 0) - 40}};
        return new Matrix(gradient);
    }

    @Override
    public Matrix hesseMatrix(Matrix theta) {
        double[][] hesseMatrix = {{2, 0},{0, 20}};
        return new Matrix(hesseMatrix);
    }
}
