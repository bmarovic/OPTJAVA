public class ElmanNN extends FFANN implements NeuralNetwork{

    double[] context;

    public ElmanNN(int[] layers, ITransferFunction[] transferFunctions, Dataset dataset) {
        layers[0] += layers[1];
        this.layers = layers;
        this.transferFunctions = transferFunctions;
        this.dataset = dataset;
        for (int i = 1; i < layers.length; i++) {
            numberOfNeurons += layers[i];
            numberOfWeights += (layers[i - 1] + 1) * layers[i];
        }
        this.neuralExits = new double[numberOfNeurons];
        this.context = new double[layers[1]];
    }

    @Override
    public double calculateError(double[] weights) {
        double error = 0;
        double[] outputs = new double[dataset.numberOfOutputs()];
        double[] inputWithContext = new double[dataset.numberOfFeatures() + context.length];
        for (int i = 0; i < dataset.numberOfSamples(); i++) {
            System.arraycopy(dataset.getInputAtIndex(i), 0, inputWithContext,
                    0, dataset.numberOfFeatures());
            System.arraycopy(neuralExits, 0, inputWithContext,
                    dataset.numberOfFeatures(), context.length);
            calcOutputs(inputWithContext, weights, outputs);
            double realOutput = dataset.getOutputAtIndex(i);
            error += Math.pow(realOutput - outputs[0], 2);
        }
        return error / dataset.numberOfSamples();
    }
}
