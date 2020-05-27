public class Dataset {

    private double[][] inputs;
    private double[][] outputs;

    public Dataset(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public int numberOfSamples(){
        return inputs.length;
    }

    public int numberOfFeatures(){
        return inputs[0].length;
    }

    public int numberOfOutputs(){
        return outputs[0].length;
    }

    public double[] getInputAtIndex(int index){
        return inputs[index];
    }

    public double[] getOutputAtIndex(int index){
        return outputs[index];
    }

}
