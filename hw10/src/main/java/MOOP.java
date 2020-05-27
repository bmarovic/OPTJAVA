import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class MOOP {

    static double[] maxValues;
    static double[] minValues;
    static double[] maxObjectives;
    static double[] minObjectives;
    static int populationSize;
    static MOOPProblem moopProblem;

    public static void main(String[] args) {

        int problemNumber = Integer.parseInt(args[0]);
        populationSize = Integer.parseInt(args[1]);
        int maxIter = Integer.parseInt(args[2]);

        Random rand = new Random();

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
        List<Chromosome> childrenPopulation = new ArrayList<>();
        initializePopulation(population, rand);
        initializePopulation(childrenPopulation, rand);

        List<Chromosome> unitedPopulation = new ArrayList<>();
        for (int i = 0; i < maxIter; i++) {


            unitedPopulation.addAll(population);
            if (i == 0) {
                unitedPopulation.addAll(childrenPopulation);
            }

            while (childrenPopulation.size() != population.size()) {
                Chromosome[] parents = CrowdingTournamentSelection.select(population);
                Chromosome firstParent = parents[0];
                Chromosome secondParent = parents[1];
                Chromosome child = BLXAlphaCrossover.crossover(firstParent, secondParent);
                Mutation.mutate(child);
                validate(child);
                moopProblem.evaluateSolution(child.getValues(), child.getObjectives());
                child.setDominatedBy(0);
                child.setDominating(new HashSet<>());
                childrenPopulation.add(child);
                unitedPopulation.add(child);
            }
            population.clear();

            List<List<Chromosome>> fronts = calculateFronts(unitedPopulation);

            FunctionComparator comparator = new FunctionComparator();
            int numberOfObjectives = moopProblem.getNumberOfObjectives();
            for (List<Chromosome> front : fronts) {
                front.forEach((chromosome -> chromosome.setCrowdingDistance(0)));
                for (int j = 0; j < numberOfObjectives; j++) {
                    FunctionComparator.setIndex(j);
                    front.sort(comparator);

                    Chromosome max = front.get(front.size() - 1);
                    Chromosome min = front.get(0);
                    double range = max.getObjectives()[j] - min.getObjectives()[j];

                    max.addToCrowdingDistance(1E9);
                    min.addToCrowdingDistance(1E9);
                    if (front.size() < 3) {
                        continue;
                    }

                    for (int k = 1; k < front.size() - 1; k++) {
                        Chromosome chromosome = front.get(k);
                        chromosome.addToCrowdingDistance((front.get(k + 1).getObjectiveAtIndex(j)
                                        - front.get(k - 1).getObjectiveAtIndex(j)) / range);
                    }
                }
            }

            for (List<Chromosome> front : fronts) {
                if (front.size() + population.size() <= populationSize) {
                    population.addAll(front);
                } else {
                    front.sort(Chromosome::compareTo);
                    int index = front.size() - 1;
                    while (population.size() < populationSize) {
                        population.add(front.get(index--));
                    }
                    break;
                }

            }

            population.forEach((chromosome) -> chromosome.setFront(0));

            childrenPopulation.clear();
            unitedPopulation.clear();

        }
        List<List<Chromosome>> fronts = calculateFronts(population);
        for (int i = 0; i < fronts.size(); i++) {
            System.out.println("Front " + (i + 1) + ": " + fronts.get(i).size() + " solutions");
        }

        try {
            PrintWriter writer1 = new PrintWriter("izlaz-dec.txt");
            PrintWriter writer2 = new PrintWriter("izlaz-obj.txt");
            writer1.write("f1, f2, front\n");
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

    private static void initializePopulation(List<Chromosome> population, Random rand) {

        for (int i = 0; i < populationSize; i++) {
            Chromosome member = new Chromosome(moopProblem.getNumberOfObjectives());
            member.randomize(rand, minValues, maxValues);
            moopProblem.evaluateSolution(member.getValues(), member.getObjectives());
            member.setDominatedBy(0);
            member.setDominating(new HashSet<>());
            population.add(member);
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

    public static List<List<Chromosome>> calculateFronts(List<Chromosome> population) {
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
        int frontCounter = 1;
        for (List<Chromosome> front : fronts) {
            for (Chromosome chromosome : front) {
                chromosome.setFront(frontCounter);
            }
            population.addAll(front);
            frontCounter++;
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

    private static class FunctionComparator implements Comparator<Chromosome>{

        private static int index;

        public static void setIndex(int i) {
            index = i;
        }

        @Override
        public int compare(Chromosome o1, Chromosome o2) {
            return Double.compare(o1.getObjectiveAtIndex(index), o2.getObjectiveAtIndex(index));
        }
    }
}
