import Jama.Matrix;

public interface IFunction {

    int numberOfVariables();
    double valueOfFunction(Matrix theta, int row);
    Matrix valueOfGradient(Matrix variables);
}
