package hr.fer.zemris.optjava.dz12;

import hr.fer.zemris.optjava.dz12.gp.Chromosome;
import hr.fer.zemris.optjava.dz12.gp.GeneticAlgorithm;
import hr.fer.zemris.optjava.dz12.nodes.Node;
import hr.fer.zemris.optjava.dz12.nodes.actions.ActionNode;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class AntTrailGA{

    static World world;
    public static final int MAX_DEPTH = 20;
    public static final int MAX_NODES = 200;

    public static void main(String[] args) {
        if(args.length != 5) {
            System.out.println("Invalid number of arguments! Should be 5.");
            System.exit(1);
        }

        String inputPath = args[0];
        int maxIter = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        double errorThreshold = Double.parseDouble(args[3]);
        String outputPath = args[4];

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(AntTrailGA.class.getClassLoader()
                                            .getResourceAsStream(inputPath))),
                            "UTF-8"));

            String[] dimensions = br.readLine().split("x");
            boolean[][] worldMap = new boolean[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
            String line = br.readLine();
            int rowIndex = 0;
            while (line != null && !line.equals("")) {
                String[] row = line.split("");
                for (int i = 0; i < row.length; i++) {
                    worldMap[rowIndex][i] = row[i].equals("1");
                }
                rowIndex++;
                line = br.readLine();
            }
            world = new World(worldMap);

        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(populationSize, maxIter, world, errorThreshold);
        Chromosome bestChromosome = geneticAlgorithm.run();
        List<ActionNode> bestChromosomeRoute = visualizationRun(world, bestChromosome);

        try {
            PrintWriter writer = new PrintWriter(outputPath);
            Node bestRoot = bestChromosome.getRoot();
            bestRoot.exportPrint(writer);
            writer.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> {
            WorldFrame frame = new WorldFrame(world, bestChromosomeRoute);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }

    public static List<ActionNode> visualizationRun(World world, Chromosome bestChromosome){
        World worldCopy = world.duplicate();
        worldCopy.setVisualization(true);
        while (worldCopy.getSteps() < World.MAX_STEPS) {
            bestChromosome.getRoot().executeOrder(worldCopy);
        }
        return worldCopy.getStepsList();
    }

}
