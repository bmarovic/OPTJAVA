import java.util.Arrays;

public class FFANN {

    private int[] layers;
    private ITransferFunction[] transferFunctions;
    private Dataset dataset;
    private int numberOfNeurons = 0;
    private int numberOfWeights = 0;
    private double[] neuralExits;

    public FFANN(int[] layers, ITransferFunction[] transferFunctions, Dataset dataset) {
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.dataset = dataset;
        for (int i = 1; i < layers.length; i++) {
            numberOfNeurons += layers[i];
            numberOfWeights += (layers[i - 1] + 1) * layers[i];
        }
        neuralExits = new double[numberOfNeurons];
    }

    public double calculateError(double[] weights){
        double error = 0;
        double[] outputs = new double[dataset.numberOfOutputs()];
        for (int i = 0; i < dataset.numberOfSamples(); i++) {
            calcOutputs(dataset.getInputAtIndex(i), weights, outputs);
            double[] realOutputs = dataset.getOutputAtIndex(i);
            for (int j = 0; j < dataset.numberOfOutputs(); j++) {
                error += Math.pow(realOutputs[j] - outputs[j], 2);
            }
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

    public void printStatistics(double[] bestSolution){

        int correctlyPredicted = 0;
        int incorrectlyPredicted = 0;
        double[] output = new double[dataset.numberOfOutputs()];

        for (int i = 0; i < dataset.numberOfSamples(); i++) {
            calcOutputs(dataset.getInputAtIndex(i), bestSolution, output);
            roundOutput(output);

            boolean correctlyClassified = true;
            for (int j = 0; j < output.length; j++) {
                if (output[j] != dataset.getOutputAtIndex(i)[j]){
                    correctlyClassified = false;
                    break;
                }
            }
            if(correctlyClassified) correctlyPredicted++;
            else incorrectlyPredicted++;

            String sb = "ANN result: " +
                    Arrays.toString(output) +
                    " Real result: " +
                    Arrays.toString(dataset.getOutputAtIndex(i));
            System.out.println(sb);
        }
        String sb = "Correctly classified: " +
                correctlyPredicted +
                " Incorrectly classified: " +
                incorrectlyPredicted;
        System.out.println(sb);

    }

    private void roundOutput(double[] output){
        for (int i = 0; i < output.length; i++) {
            if(output[i] >= 0.5) output[i] = 1;
            else output[i] = 0;
        }
    }

}
