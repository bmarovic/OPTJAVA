public class FFANN implements NeuralNetwork{

    int[] layers;
    ITransferFunction[] transferFunctions;
    Dataset dataset;
    int numberOfNeurons = 0;
    int numberOfWeights = 0;
    double[] neuralExits;

    public FFANN(int[] layers, ITransferFunction[] transferFunctions, Dataset dataset) {
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.dataset = dataset;
        for (int i = 1; i < layers.length; i++) {
            numberOfNeurons += layers[i];
            numberOfWeights += (layers[i - 1] + 1) * layers[i];
        }
        this.neuralExits = new double[numberOfNeurons];
    }

    FFANN() {
    }

    public double calculateError(double[] weights){
        double error = 0;
        double[] outputs = new double[dataset.numberOfOutputs()];
        for (int i = 0; i < dataset.numberOfSamples(); i++) {
            calcOutputs(dataset.getInputAtIndex(i), weights, outputs);
            double realOutput = dataset.getOutputAtIndex(i);
            error += Math.pow(realOutput - outputs[0], 2);
        }
        return error / dataset.numberOfSamples();
    }

    public int getWeightsCount(){
        return numberOfWeights;
    }

    public void calcOutputs(double[] inputs, double[] weights, double[] outputs){

        int neuronNumber = 0;
        int neuronIndex = numberOfNeurons;

        for (int i = 1; i < layers.length; i++) {
            int exitsStart = neuronNumber - layers[i - 1];
            for (int j = 0; j < layers[i]; j++) {
                double net = weights[neuronNumber];
                for (int k = neuronIndex; k < layers[i - 1] + neuronIndex; k++) {
                    if(i == 1){
                        net += weights[k] * inputs[k - neuronIndex];
                    }else{
                        net += weights[k] * neuralExits[exitsStart + k - neuronIndex];
                    }
                }
                neuralExits[neuronNumber] = transferFunctions[i - 1].valueOf(net);
                neuronIndex += layers[i - 1];
                neuronNumber++;
            }
        }
        System.arraycopy(neuralExits, neuralExits.length - outputs.length, outputs, 0, outputs.length);
    }

}
