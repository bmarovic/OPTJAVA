import Jama.Matrix;

public interface IHFunction extends IFunction {

    Matrix hesseMatrix(Matrix theta);
}
