import Jama.Matrix;



public class FunctionSystem implements IHFunction{

    Matrix xValues;
    Matrix yValues;

    public FunctionSystem(Matrix xValues, Matrix yValues) {

        this.xValues = xValues;
        this.yValues = yValues;
    }

    @Override
    public int numberOfVariables() {
        return 10;
    }

    @Override
    public double valueOfFunction(Matrix theta, int row) {

        return theta.times(xValues.getMatrix(new int[]{row},
                new int[]{0,1,2,3,4,5,6,7,8,9}).transpose()).get(0,0);

    }

    @Override
    public Matrix valueOfGradient(Matrix theta) {

        return xValues.transpose().times(xValues.times(theta).minus(yValues)).times(2);

    }

    @Override
    public Matrix hesseMatrix(Matrix theta) {

        return xValues.transpose().times(xValues);

    }

}
