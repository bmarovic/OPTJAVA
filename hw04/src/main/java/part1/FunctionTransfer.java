package part1;

public class FunctionTransfer implements IFunction {

    private double[][] xValues;
    private double[] yValues;

    public FunctionTransfer(double[][] xValues, double[] yValues) {

        this.xValues = xValues;
        this.yValues = yValues;
    }

    @Override
    public double valueAt(double[] vector) {
        double valueOfCost = 0;

        for (int i = 0; i < xValues.length; i++) {
            double functionValue = vector[0] * xValues[i][0]
                    + vector[1] * Math.pow(xValues[i][0], 3) * xValues[i][1]
                    + vector[2] * Math.exp(vector[3] * xValues[i][2]) * (1 + Math.cos(vector[4] * xValues[i][3]))
                    + vector[5] * xValues[i][3] * Math.pow(xValues[i][4], 2);

            valueOfCost += Math.pow(functionValue - yValues[i], 2);
        }
        return valueOfCost * 1/40;
    }

}
