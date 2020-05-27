import Jama.Matrix;

public class FunctionTransfer implements IHFunction{

    private Matrix xValues;
    private Matrix yValues;
    private static double a, b, c, d, e, f, x1, x2, x3, x4, x5, y;


    public FunctionTransfer(Matrix xValues, Matrix yValues) {

        this.xValues = xValues;
        this.yValues = yValues;
    }


    @Override
    public int numberOfVariables() {
        return 6;
    }

    @Override
    public double valueOfFunction(Matrix theta, int row) {

        initializeVariables(theta,row);

        return a * x1 + b * Math.pow(x1, 3) * x2 + c * Math.exp(d * x3) * (1 + Math.cos(e * x4))
                + f * x4 * Math.pow(x5, 2);
    }

    @Override
    public Matrix valueOfGradient(Matrix theta) {

        Matrix gradient = new Matrix(theta.getRowDimension(), 1);

        for (int i = 0; i < xValues.getRowDimension(); i++) {
                for (int k = 0; k < gradient.getRowDimension(); k++) {

                    int[] columns = new int[]{0, 1, 2, 3, 4};
                    double matrixValue = gradient.get(k, 0);
                    gradient.set(k, 0, (valueOfFunction(theta, i) - yValues.get(i, 0))
                            * partialDerivative(k, theta, i) + matrixValue);

                }
        }
        return gradient.times(1.0/(gradient.normInf()));
    }

    public double partialDerivative(int variable, Matrix theta, int row){

        initializeVariables(theta,row);

        switch (variable){
            case 0:
                return 2 * x1;
            case 1:
                return 2 * Math.pow(x1, 3) * x2;
            case 2:
                return 2 * Math.exp(d * x3) * (1 + Math.cos(e * x4));
            case 3:
                return 2 * c * x3 * Math.exp(d * x3) * (1 + Math.cos(e * x4));
            case 4:
                return 2 *  c * Math.exp(d * x3) * x4 * - Math.sin(e * x4);
            case 5:
                return 2 * x4 * Math.pow(x5, 2);
        }
        return 0;
    }

    @Override
    public Matrix hesseMatrix(Matrix theta) {

        Matrix hesse;
        double[][] hesseIteration = new double[numberOfVariables()][numberOfVariables()];
        for (int row = 0; row < xValues.getRowDimension(); row++) {

            initializeVariables(theta, row);

            hesseIteration[0][0] += 2 * Math.pow(x1, 2);
            hesseIteration[0][1] += 2 * Math.pow(x1, 4) * x2;
            hesseIteration[0][2] += 2 * x1 * Math.exp(d*x3)*(Math.cos(e*x4) + 1);
            hesseIteration[0][3] += 2 * c * x1 * x3 * Math.exp(d*x3) * (Math.cos(e*x4) + 1);
            hesseIteration[0][4] += - 2 * c * x1 * x4 * Math.exp(d*x3) * Math.sin(e*x4);
            hesseIteration[0][5] += 2 * x1 * x4 * Math.pow(x5, 2);

            hesseIteration[1][1] += 2 * Math.pow(x1, 6) * Math.pow(x2, 2);
            hesseIteration[1][2] += 2 * Math.pow(x1, 3) * x2 *Math.exp(d*x3)*(Math.cos(e*x4) + 1);
            hesseIteration[1][3] += 2 * c * Math.pow(x1, 3) * x2 * x3 * Math.exp(d * x3) * (Math.cos(e*x4) + 1);
            hesseIteration[1][4] += -2 * c * Math.pow(x1, 3) * x2 * x4 * Math.exp(d*x3) * Math.sin(e*x4);
            hesseIteration[1][5] += 2 * Math.pow(x1, 3) * x2 * x4 * Math.pow(x5, 2);

            hesseIteration[2][2] += 2*Math.pow(Math.exp(2*d*x3)*(Math.cos(e*x4) + 1), 2);
            hesseIteration[2][3] += 2*Math.pow(c*x3*Math.exp(2*d*x3)*(Math.cos(e*x4) + 1), 2)
                    + 2*x3*Math.exp(d*x3)*(Math.cos(e*x4) + 1)*(b*x2*Math.pow(x1, 3) + a*x1 + f*x4*Math.pow(x5,2) - y
                    + c*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
            hesseIteration[2][4] += - 2*x4*Math.exp(d*x3)*Math.sin(e*x4)*(b*x2*Math.pow(x1, 3)
                    + a*x1 + f*x4*Math.pow(x5, 2) - y + c*Math.exp(d*x3)*(Math.cos(e*x4) + 1))
                    - 2 * c * x4 * Math.exp(2*d*x3) * Math.sin(e*x4) * (Math.cos(e*x4) + 1);
            hesseIteration[2][5] += 2 * x4 * Math.pow(x5, 2) * Math.exp(d*x3)*(Math.cos(e*x4) + 1);

            hesseIteration[3][3] += 2*Math.pow(c, 2) * Math.pow(x3, 2) *Math.exp(2*d*x3)
                    * Math.pow((Math.cos(e*x4) + 1), 2) + 2*c*Math.pow(x3, 2)
                    *Math.exp(d*x3)*(Math.cos(e*x4) + 1)*(b*x2*Math.pow(x1, 3)
                    + a*x1 + f*x4*Math.pow(x5, 2) - y + c*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
            hesseIteration[3][4] += - 2*Math.pow(c, 2) * x3 * x4 * Math.exp(2*d*x3)*Math.sin(e*x4)*(Math.cos(e*x4) + 1)
                    - 2*c*x3*x4*Math.exp(d*x3) * Math.sin(e*x4) * (b*x2*Math.pow(x1, 3)
                    + a*x1 + f*x4*Math.pow(x5, 2) - y + c*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
            hesseIteration[3][5] += 2 * c * x3 * x4 * Math.pow(x5, 2)*Math.exp(d*x3)*(Math.cos(e*x4) + 1);


            hesseIteration[4][4] += 2 * Math.pow(c, 2) * Math.pow(x4, 2) * Math.exp(2*d*x3) * Math.pow(Math.sin(e*x4),2)
                    - 2 * c * Math.pow(x4, 2) * Math.exp(d*x3) * Math.cos(e * x4) * (b * x2 *Math.pow(x1, 3) + a * x1
                    + f*x4*Math.pow(x5,2) - y + c*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
            hesseIteration[4][5] += -2*c*Math.pow(x4,2)*Math.pow(x5,2)*Math.exp(d*x3)*Math.sin(e*x4);

            hesseIteration[5][5] += 2 * Math.pow(x4, 2) * Math.pow(x5, 4);

            for (int i = 0; i < numberOfVariables(); i++) {
                for (int j = 0; j < i; j++) {
                    hesseIteration[i][j] = hesseIteration[j][i];
                }
            }
        }

        hesse = new Matrix(hesseIteration);
        return hesse.times(1.0 / (2 * xValues.getRowDimension()));
    }


    public void initializeVariables(Matrix theta, int row){

        a = theta.get(0,0);
        b = theta.get(1,0);
        c = theta.get(2,0);
        d = theta.get(3,0);
        e = theta.get(4,0);
        f = theta.get(5,0);

        y = yValues.get(row, 0);

        x1 = xValues.get(row, 0);
        x2 = xValues.get(row, 1);
        x3 = xValues.get(row, 2);
        x4 = xValues.get(row, 3);
        x5 = xValues.get(row, 4);

    }

}
