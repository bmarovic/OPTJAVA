package part2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class Chromosome implements Comparable<Chromosome>{

    private ArrayList<Integer> factoryDistribution = new ArrayList<>();
    private double error;

    public Chromosome(int numberOfEntities) {
        for (int i = 0; i < numberOfEntities; i++) {
            factoryDistribution.add(i);
        }
    }

    public Chromosome(ArrayList<Integer> factoryDistribution) {
        this.factoryDistribution = factoryDistribution;
    }

    public void randomize(){
        Collections.shuffle(factoryDistribution);
    }

    public ArrayList<Integer> getFactoryDistribution() {
        return factoryDistribution;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chromosome that = (Chromosome) o;
        return Objects.equals(factoryDistribution, that.factoryDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(factoryDistribution);
    }


    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.error, o.error);
    }

    @Override
    public String toString() {
        return factoryDistribution + " error = " + error;
    }
}
