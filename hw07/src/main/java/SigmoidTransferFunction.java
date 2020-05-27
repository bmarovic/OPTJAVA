public class SigmoidTransferFunction implements ITransferFunction{

    public double valueOf(double input){
        return 1.0/(1.0 + Math.exp(- input));
    }

}
