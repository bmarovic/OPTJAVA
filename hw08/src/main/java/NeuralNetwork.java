public interface NeuralNetwork {
    void calcOutputs(double[] inputs, double[] weights, double[] outputs);
    double calculateError(double[] weights);
    int getWeightsCount();
}
