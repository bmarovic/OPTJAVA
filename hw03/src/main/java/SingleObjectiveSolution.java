public class SingleObjectiveSolution {

    public double fitness;
    public double value;

    public int compareTo(SingleObjectiveSolution solution){
        return Double.compare(this.fitness, solution.fitness);
    }
}
