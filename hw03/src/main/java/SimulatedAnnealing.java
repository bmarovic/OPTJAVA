import java.util.Random;

public class SimulatedAnnealing<T> implements IOptAlgorithm{

    private IDecoder<T> decoder;
    private INeighborhood<T> neighborhood;
    private T startWith;
    private IFunction function;
    private boolean minimize;
    private Random rand = new Random();
    private ITempSchedule tempSchedule;



    public SimulatedAnnealing(IDecoder<T> decoder, INeighborhood<T> neighborhood,
                              T startWith, IFunction function,
                              boolean minimize, ITempSchedule tempSchedule) {
        this.decoder = decoder;
        this.neighborhood = neighborhood;
        this.startWith = startWith;
        this.function = function;
        this.minimize = minimize;
        this.tempSchedule = tempSchedule;

    }


    @Override
    public void run() {
        T currentSolution = startWith;
        double[] parameters = decoder.decode(startWith);
        double currentValueOfFunction = function.valueAt(parameters);
        double temperature = 0;

        int maxInner = tempSchedule.getInnerLoopCounter();
        int maxOuter = tempSchedule.getOuterLoopCounter();

        for (int i = 0; i < maxOuter; i++) {
            temperature = tempSchedule.getNextTemperature();
            for (int j = 0; j < maxInner; j++) {
                T newSolution = neighborhood.randomNeighbor(currentSolution);
                parameters = decoder.decode(newSolution);
                double newValueOfFunction = function.valueAt(parameters);
                double delta = newValueOfFunction - currentValueOfFunction;

                if (!minimize){
                    delta *= -1;
                }

                if(delta <= 0){
                    currentSolution = newSolution;
                    currentValueOfFunction = newValueOfFunction;
                }else {

                    double probability = Math.exp(-delta/temperature);
                    if (probability > rand.nextDouble()){
                        currentSolution = newSolution;
                        currentValueOfFunction = newValueOfFunction;
                    }
                }
                parameters = decoder.decode(currentSolution);
                StringBuilder sb = new StringBuilder();
                sb.append("Temp: ").append(temperature).append(", [ ");
                for (double x : parameters){
                    sb.append(x).append(" ");
                }
                sb.append("] Error: ").append(currentValueOfFunction);
                System.out.println(sb.toString());
            }
        }


    }
}
