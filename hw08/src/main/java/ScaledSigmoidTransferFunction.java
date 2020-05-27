public class ScaledSigmoidTransferFunction implements ITransferFunction {
    public double valueOf(double input){
        return 2 /(1 + Math.exp(- input)) - 1;
    }
}
