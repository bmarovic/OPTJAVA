import Jama.Matrix;

public class Function1 implements IHFunction{


    @Override
    public int numberOfVariables() {
        return 2;
    }

    @Override
    public double valueOfFunction(Matrix vector, int row) {
        return Math.pow(vector.get(0, 0),2) - Math.pow(vector.get(1, 0) - 1, 2);
    }

    @Override
    public Matrix valueOfGradient(Matrix vector) {
        double[][] gradient = {{2 * vector.get(0, 0)}, {2 * vector.get(1, 0) - 2}};
        return new Matrix(gradient);
    }

    @Override
    public Matrix hesseMatrix(Matrix theta) {
        double[][] hesseMatrix = {{2, 0},{0, 2}};
        return new Matrix(hesseMatrix);
    }
}
