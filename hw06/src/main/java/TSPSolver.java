import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class TSPSolver {

    private static final double EVAPORATION_RATE = 0.04;
    static final double ALPHA = 1;
    private static final double BETA = 5;
    private static double MAX_PHEROMONE;
    private static double MIN_PHEROMONE;
    static final double A = 100;
    private static Random rand = new Random();

    public static void main(String[] args) {

        if(args.length != 4){
            System.exit(1);
        }

        int candidateListSize = Integer.parseInt(args[1]);
        int numberOfAnts = Integer.parseInt(args[2]);
        int maxIter = Integer.parseInt(args[3]);
        int numberOfCities = 0;
        ArrayList<City> cities = new ArrayList<>();

        try {
            String path = args[0];
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new BufferedInputStream(
                                    Objects.requireNonNull(TSPSolver.class.getClassLoader()
                                            .getResourceAsStream(path))),
                            "UTF-8"));


            String line = br.readLine();
            while (!line.startsWith("DIMENSION")){
                line = br.readLine();
            }
            numberOfCities = Integer.parseInt(line.split(" ")[2]);
            while (!line.trim().startsWith("1 ")){
                line = br.readLine();
            }

            int cityID = 0;
            while(!line.equals("EOF")){
                String[] coordinates = line.trim()
                        .replace("    ", " ")
                        .replace("   ", " ")
                        .replace("  ", " ")
                        .split(" ");
                cities.add(new City(Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[2]), cityID++));
                line = br.readLine();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        ArrayList<ArrayList<Double>> distanceMatrix = new ArrayList<>();
        ArrayList<ArrayList<Double>> etaBetaMatrix = new ArrayList<>();

        int stagnationCounter = 0;

        for (int i = 0; i < numberOfCities; i++) {
            ArrayList<Double> rowDistance = new ArrayList<>();
            ArrayList<Double> rowEta =  new ArrayList<>();
            for (int j = 0; j < numberOfCities; j++) {
                double distance = cities.get(i).cartesianDistance(cities.get(j));
                rowDistance.add(distance);
                rowEta.add(Math.pow(1.0 / distance, BETA));
            }
            distanceMatrix.add(rowDistance);
            etaBetaMatrix.add(rowEta);
        }

        for (City city : cities) {
            city.calculateCandidateList(candidateListSize, distanceMatrix, cities);
        }

        double[][] pheromoneMatrix = new double[numberOfCities][numberOfCities];

        Ant greedyAnt = new Ant(cities.get(rand.nextInt(numberOfCities)));
        greedyAnt.greedyRun(distanceMatrix, cities);

        MAX_PHEROMONE = 1 / (EVAPORATION_RATE * greedyAnt.getTripLength());
        MIN_PHEROMONE = MAX_PHEROMONE / A;

        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) {
                pheromoneMatrix[i][j] = MAX_PHEROMONE;
            }
        }

        Ant bestAnt = null;

        for (int i = 0; i < maxIter; i++) {

            ArrayList<Ant> colony = new ArrayList<>();
            for (int j = 0; j < numberOfAnts; j++) {
                colony.add(new Ant(cities.get(rand.nextInt(numberOfCities))));
            }
            for (Ant ant : colony) {
                ant.run(distanceMatrix, etaBetaMatrix, pheromoneMatrix, cities);
            }

            for (int j = 0; j < pheromoneMatrix.length; j++) {
                for (int k = 0; k < pheromoneMatrix.length; k++) {
                    pheromoneMatrix[j][k] *= (1 - EVAPORATION_RATE);
                    if (pheromoneMatrix[j][k] < MIN_PHEROMONE){
                        pheromoneMatrix[j][k] = MIN_PHEROMONE;
                    }
                }
            }

            Ant iterBestAnt = colony.stream().min(Ant::compareTo).get();

            if (bestAnt == null) bestAnt = iterBestAnt;
            else if (iterBestAnt.compareTo(bestAnt) < 0) {
                bestAnt = iterBestAnt;
                stagnationCounter = 0;
                MAX_PHEROMONE = 1 / (EVAPORATION_RATE * bestAnt.getTripLength());
                MIN_PHEROMONE = MAX_PHEROMONE / A;
            }
            else{
                stagnationCounter++;
            }

            if (i < 3 * maxIter / 4) updateBestPheromone(pheromoneMatrix, bestAnt);
            else {
                if (rand.nextDouble() > 0.5) {
                    updateBestPheromone(pheromoneMatrix, iterBestAnt);
                } else {
                    updateBestPheromone(pheromoneMatrix, bestAnt);
                }
            }

            if (stagnationCounter > maxIter * 0.05) {
                for (int j = 0; j < pheromoneMatrix.length; j++) {
                    for (int k = 0; k < pheromoneMatrix.length; k++) {
                        pheromoneMatrix[j][k] = MAX_PHEROMONE;
                    }
                }
                stagnationCounter = 0;
            }
            System.out.println(bestAnt);
        }
    }

    private static void updateBestPheromone(double[][] pheromoneMatrix, Ant bestAnt) {
        ArrayList<City> bestRoute = bestAnt.getVisitedCities();
        for (int j = 0; j < bestRoute.size() - 1; j++) {
            int indexFrom = bestRoute.get(j).getCityID();
            int indexTo = bestRoute.get(j + 1).getCityID();
            pheromoneMatrix[indexFrom][indexTo] += 1 / bestAnt.getTripLength();
            pheromoneMatrix[indexTo][indexFrom] += 1 / bestAnt.getTripLength();
            if (pheromoneMatrix[indexFrom][indexTo] > MAX_PHEROMONE) {
                pheromoneMatrix[indexFrom][indexTo] = MAX_PHEROMONE;
                pheromoneMatrix[indexTo][indexFrom] = MAX_PHEROMONE;
            }
        }
    }

}
