import java.util.Random;

public class WeightVectorSolution implements Comparable<WeightVectorSolution>{

    private double[] values;
    private double error;
    private double age;

    public WeightVectorSolution(int numberOfValues) {
        this.values = new double[numberOfValues];
        this.age = 0;
    }

    public WeightVectorSolution newLikeThis(){
        return new WeightVectorSolution(values.length);
    }

    public WeightVectorSolution duplicate(){
        WeightVectorSolution copy = new WeightVectorSolution(values.length);
        System.arraycopy(this.values, 0, copy.values, 0, values.length);
        return copy;
    }

    public void randomize(Random rand, double lowerLimit, double upperLimit){
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextDouble() * (upperLimit - lowerLimit) + lowerLimit;
        }
    }

    public void mutate(double mutationRate, Random rand){
        for (int i = 0; i < values.length; i++) {
            values[i] += (2*rand.nextDouble() - 1) * mutationRate;
        }
    }


    @Override
    public int compareTo(WeightVectorSolution o) {
        return Double.compare(this.error, o.error);
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public void increaseAge(){
        this.age++;
    }

    public double getAge() {
        return age;
    }

    public void resetAge() {
        this.age = 0;
    }
}
