import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class Chromosome implements Comparable<Chromosome>{

    private double[] values;
    private double[] objectives;
    private double fitness;

    private Set<Chromosome> dominating;
    private int dominatedBy;

    public Chromosome(int numberOfValues) {
        this.values = new double[numberOfValues];
        this.objectives = new double[numberOfValues];
    }

    public void randomize(Random rand, double[] lowerLimits, double[] upperLimits){
        for (int i = 0; i < values.length; i++) {
            values[i] = rand.nextDouble() * (upperLimits[i] - lowerLimits[i]) + lowerLimits[i];
        }
    }

    public int numberOfVariables(){
        return values.length;
    }

    public double getAtIndex(int index){
        return values[index];
    }

    public void setAtIndex(double value, int index){
        values[index] = value;
    }

    public double[] getValues() {
        return values;
    }

    public double[] getObjectives() {
        return objectives;
    }

    public Set<Chromosome> getDominating() {
        return dominating;
    }

    public void setDominating(Set<Chromosome> dominating) {
        this.dominating = dominating;
    }

    public int getDominatedBy() {
        return dominatedBy;
    }

    public void setDominatedBy(int dominatedBy) {
        this.dominatedBy = dominatedBy;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void incrementDominatedBy() {
        this.dominatedBy++;
    }

    public void decrementDominatedBy() {
        this.dominatedBy--;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.fitness, o.fitness);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Arrays.equals(values, that.values) &&
                Arrays.equals(objectives, that.objectives);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(values);
        result = 31 * result + Arrays.hashCode(objectives);
        return result;
    }
}
