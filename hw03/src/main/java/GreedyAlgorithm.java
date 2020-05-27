public class GreedyAlgorithm<T> implements IOptAlgorithm{

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;
    private int maxIterations;

    public GreedyAlgorithm(IDecoder<T> decoder, INeighborhood<T> neighborhood, T startWith,
                           IFunction function, boolean minimize) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
    }


    @Override
    public void run() {
        T currentSolution = startWith;
        double[] parameters = decoder.decode(currentSolution);
        double currentValueOfFunction = function.valueAt(parameters);

        for (int i = 0; i < maxIterations; i++) {
            T neighbor = neighborhood.randomNeighbor(currentSolution);
            parameters = decoder.decode(neighbor);
            double newValueOfFunction = function.valueAt(parameters);
            double delta = newValueOfFunction - currentValueOfFunction;
            if (!minimize){
                delta *= -1;
            }

            if (delta < 0){
                currentSolution = neighbor;
                currentValueOfFunction = newValueOfFunction;
            }
        }
        parameters = decoder.decode(currentSolution);
        for (double var: parameters) {
            System.out.println(var + " ");
        }
    }
}
