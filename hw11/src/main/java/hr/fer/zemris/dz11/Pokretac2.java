package hr.fer.zemris.dz11;

import hr.fer.zemris.art.GrayScaleImage;
import hr.fer.zemris.generic.ga.*;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Pokretac2 {

    public static void main(String[] args) throws IOException {
        if (args.length != 7) {
            System.out.println("Invalid number of arguments!");
            System.exit(1);
        }

        GrayScaleImage image = GrayScaleImage.load(new File(args[0]));
        int numberOfRectangles = Integer.parseInt(args[1]);
        int populationSize = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);
        double errorThreshold = Double.parseDouble(args[4]);
        String parameterExportPath = args[5];
        File imageExportPath = new File(args[6]);
        Evaluator evaluator = new Evaluator(image);

        Mutation mutation = new Mutation(image);
        ICrossover crossover = new KBreakpointCrossover(10);
        RouletteWheelSelection selection = new RouletteWheelSelection();

        ParallelChildrenGA ga = new ParallelChildrenGA(selection, crossover, mutation, errorThreshold, maxIter,
                numberOfRectangles, image, populationSize);

        Chromosome bestChromosome = ga.run();

        GrayScaleImage imageBest = new GrayScaleImage(image.getWidth(), image.getHeight());
        imageBest = evaluator.draw(bestChromosome, imageBest);
        imageBest.save(imageExportPath);

        PrintWriter writer = new PrintWriter(parameterExportPath);
        int[] parameters = bestChromosome.getData();
        int size = parameters.length;
        for (int parameter : parameters) {
            writer.write(parameter + "\n");
        }
        writer.close();
    }
}