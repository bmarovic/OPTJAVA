import java.util.Arrays;
import java.util.Random;
import java.util.Set;

public class Chromosome implements Comparable<Chromosome>{

    private double[] values;
    private double[] objectives;
    private double crowdingDistance = 0;


    private int front;

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

    public double getObjectiveAtIndex(int index){ return values[index]; }

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

    public void incrementDominatedBy() {
        this.dominatedBy++;
    }

    public void decrementDominatedBy() {
        this.dominatedBy--;
    }

    public void addToCrowdingDistance(double value) {
        crowdingDistance += value;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public int getFront() {
        return front;
    }

    public void setFront(int front) {
        this.front = front;
    }




    @Override
    public int compareTo(Chromosome o) {
        if (o.front > this.front) {
            return 1;
        } else if (o.front < this.front) {
            return -1;
        } else {
            return Double.compare(this.crowdingDistance, o.crowdingDistance);
        }
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
