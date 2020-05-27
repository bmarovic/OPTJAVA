package hr.fer.zemris.optjava.dz12.gp;

import hr.fer.zemris.optjava.dz12.nodes.Node;

public class Chromosome implements Comparable<Chromosome> {

    private double fitness;
    private Node root;
    private int depth;

    public Chromosome(Node root) {
        this.root = root;
        this.depth = root.maxDepth();
    }

    public Chromosome() {
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getDepth() {
        return root.maxDepth();
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public int compareTo(Chromosome o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public Chromosome copy(){
        Chromosome temp = new Chromosome();
        temp.setRoot(this.getRoot().copy());
        temp.setFitness(this.getFitness());
        temp.setDepth(this.depth);
        return temp;
    }


}
