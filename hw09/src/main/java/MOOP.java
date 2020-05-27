import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MOOP {

    static double[] maxValues;
    static double[] minValues;
    static double[] maxObjectives;
    static double[] minObjectives;
    static double epsilon = 0.9;

    public static void main(String[] args) {

        int problemNumber = Integer.parseInt(args[0]);
        int populationSize = Integer.parseInt(args[1]);
        boolean typeOfProgram = false;
        if (args[2].equals("decision-space")) {
            typeOfProgram = true;
        } else if (args[2].equals("objective-space")) {
            typeOfProgram = false;
        } else System.exit(1);
        int maxIter = Integer.parseInt(args[3]);

        Random rand = new Random();
        MOOPProblem moopProblem = null;


        if (problemNumber == 1) {
            moopProblem = new Problem1();
            maxValues = new double[]{5, 5, 5, 5};
            minValues = new double[]{-5, -5, -5, -5};
            maxObjectives = new double[]{25, 25, 25, 25};
            minObjectives = new double[]{0, 0, 0, 0};
        } else if (problemNumber == 2) {
            moopProblem = new Problem2();
            maxValues = new double[]{1, 5};
            minValues = new double[]{0.1, 0};
            maxObjectives = new double[]{1, 60};
            minObjectives = new double[]{0.1, 1};
        } else {
            System.out.printf("Invalid problem number, 1 or 2 required.");
            System.exit(1);
        }

        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Chromosome member = new Chromosome(moopProblem.getNumberOfObjectives());
            member.randomize(rand, minValues, maxValues);
            moopProblem.evaluateSolution(member.getValues(), member.getObjectives());
            member.setDominatedBy(0);
            member.setDominating(new HashSet<>());
            population.add(member);
        }

        for (int i = 0; i < maxIter; i++) {

            List<Chromosome> newPopulation = new ArrayList<>();
            calculateFitness(population, typeOfProgram);
            while (newPopulation.size() != population.size()) {
                Chromosome firstParent = RouletteWheelSelection.selectFittest(population);
                Chromosome secondParent = RouletteWheelSelection.selectFittest(population);
                Chromosome child = BLXAlphaCrossover.crossover(firstParent, secondParent);
                Mutation.mutate(child);
                validate(child);
                moopProblem.evaluateSolution(child.getValues(), child.getObjectives());
                child.setDominatedBy(0);
                child.setDominating(new HashSet<>());
                newPopulation.add(child);
            }

            population = newPopulation;
        }
        List<List<Chromosome>> fronts = calculateFitness(population, typeOfProgram);
        for (int i = 0; i < fronts.size(); i++) {
            System.out.println("Front " + (i + 1) + ": " + fronts.get(i).size() + " solutions");
        }

        try {
            PrintWriter writer1;
            PrintWriter writer2;
            if (typeOfProgram) {
                writer1 = new PrintWriter("izlaz-dec.txt");
                writer2 = new PrintWriter("izlaz-obj.txt");
            } else {
                writer1 = new PrintWriter("izlaz-dec.txt");
                writer2 = new PrintWriter("izlaz-obj.txt");
            }
            writer2.write("f1, f2, front\n");
            for (int i = 0; i < fronts.size(); i++) {
                for (Chromosome chromosome : fronts.get(i)) {
                    StringBuilder str = new StringBuilder();
                    double[] solution = chromosome.getValues();
                    for (double v : solution) {
                        str.append(v).append(", ");
                    }
                    str.append("\n");
                    writer1.write(str.toString());
                    str = new StringBuilder();
                    double[] objectives = chromosome.getObjectives();
                    for (double objective : objectives) {
                        str.append(objective).append(", ");
                    }
                    str.append(i + 1).append("\n");
                    writer2.write(str.toString());
                }
            }
            writer1.close();
            writer2.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static boolean isDominating(Chromosome chromosome1, Chromosome chromosome2) {
        double[] objectives1 = chromosome1.getObjectives();
        double[] objectives2 = chromosome2.getObjectives();
        boolean dominating = false;

        for (int i = 0; i < objectives1.length; i++) {
            if (objectives1[i] > objectives2[i]) return false;
            if (objectives1[i] < objectives2[i]) dominating = true;
        }
        return dominating;
    }

    public static List<List<Chromosome>> calculateFitness(List<Chromosome> population, boolean typeOfProgram) {
        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.size(); j++) {
                if (i == j) continue;
                Chromosome chromosome1 = population.get(i);
                Chromosome chromosome2 = population.get(j);

                if (isDominating(chromosome1, chromosome2)){
                    chromosome2.incrementDominatedBy();
                    chromosome1.getDominating().add(chromosome2);
                }
            }
        }
        double minFitness = population.size() + epsilon;
        List<List<Chromosome>> fronts = new ArrayList<>();
        while (!population.isEmpty()) {
            List<Chromosome> front = new ArrayList<>();
            for (Chromosome chromosome : population) {
                if (chromosome.getDominatedBy() == 0) {
                    front.add(chromosome);
                }
            }

            for (Chromosome chromosome : front){
                for (Chromosome dominated : chromosome.getDominating()){
                    dominated.decrementDominatedBy();
                }
            }

            population.removeAll(front);
            fronts.add(front);
        }

        SharingFunction sharingFunction = new SharingFunction();

        for (List<Chromosome> front : fronts) {
            minFitness -= epsilon;
            for (Chromosome chromosome1 : front){
                double nc = 0;
                for (Chromosome chromosome2 : front){
                    if (typeOfProgram)
                        nc += sharingFunction.valueOfFitness(chromosome1.getValues(), chromosome2.getValues(),
                                maxValues, minValues);
                    else nc += sharingFunction.valueOfFitness(chromosome1.getObjectives(), chromosome2.getObjectives(),
                            maxObjectives, minObjectives);
                }
                chromosome1.setFitness(minFitness / nc);
            }

            for (Chromosome chromosome : front){
                if (chromosome.getFitness() < minFitness){
                    minFitness = chromosome.getFitness();
                }
            }
            population.addAll(front);
        }
        return fronts;
    }

    public static void validate(Chromosome chromosome) {
        for (int i = 0; i < chromosome.getValues().length; i++) {
            if (chromosome.getValues()[i] > maxValues[i]){
                chromosome.setAtIndex(maxValues[i], i);
                continue;
            }
            if (chromosome.getValues()[i] < minValues[i]) chromosome.setAtIndex(minValues[i], i);
        }
    }
}
