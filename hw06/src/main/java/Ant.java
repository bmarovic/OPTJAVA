import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class Ant implements Comparable<Ant>{

    private double tripLength = 0;
    private ArrayList<City> visitedCities = new ArrayList<>();
    private City currentCity;
    private City startingCity;
    private Random rand = new Random();

    public Ant(City currentCity) {
        this.currentCity = currentCity;
        this.startingCity = currentCity;
        visitedCities.add(currentCity);
    }

    public void run(ArrayList<ArrayList<Double>> distanceMatrix, ArrayList<ArrayList<Double>> etaBetaMatrix,
                    double[][] pheromoneMatrix, ArrayList<City> cities){
        while (visitedCities.size() != cities.size()){
            ArrayList<City> candidateList = currentCity.getCandidateList();
            if(!visitedCities.containsAll(candidateList)){
                travelToNextCity(etaBetaMatrix, pheromoneMatrix, candidateList);
            }else{
                ArrayList<City> otherNeighbors = new ArrayList<>(cities);
                otherNeighbors.removeAll(candidateList);
                travelToNextCity(etaBetaMatrix, pheromoneMatrix, otherNeighbors);
            }
        }
        visitedCities.add(startingCity);

        for (int i = 0; i < visitedCities.size() - 1; i++) {
            tripLength += distanceMatrix.get(visitedCities.get(i)
                    .getCityID()).get(visitedCities.get(i + 1).getCityID());
        }

    }

    private void travelToNextCity(ArrayList<ArrayList<Double>> etaBetaMatrix,
                                  double[][] pheromoneMatrix, ArrayList<City> candidateList) {
        double sum = 0;
        ArrayList<Double> probability = new ArrayList<>();
        ArrayList<City> eligibleCities = new ArrayList<>();
        for (City city : candidateList) {
            if(!visitedCities.contains(city)){
                double product = Math.pow(pheromoneMatrix[currentCity.getCityID()][city.getCityID()],
                        TSPSolver.ALPHA)
                        * etaBetaMatrix.get(currentCity.getCityID()).get(city.getCityID());
                sum += product;
                probability.add(product);
                eligibleCities.add(city);
            }
        }
        if (eligibleCities.size() == 1){
            currentCity = eligibleCities.get(0);
            visitedCities.add(currentCity);
            return;
        }
        double previousProbability = 0.0;

        for (int j = 0; j < probability.size(); j++) {

            probability.set(j, probability.get(j) / sum + previousProbability);
            previousProbability = probability.get(j);
        }

        double randomValue = rand.nextDouble();
        boolean change = false;
        if (randomValue > probability.get(0)) {
            for (int i = 0; i < probability.size() - 1; i++) {
                if (randomValue > probability.get(i) && randomValue < probability.get(i + 1)) {
                    currentCity = eligibleCities.get(i);
                    change = true;
                    break;
                }
            }
        }else {
            currentCity = eligibleCities.get(0);
            change = true;
        }

        if (!change) currentCity = eligibleCities.get(eligibleCities.size() - 1);

        visitedCities.add(currentCity);
    }

    public void greedyRun(ArrayList<ArrayList<Double>> distanceMatrix, ArrayList<City> cities){

        while (visitedCities.size() != cities.size()){
            ArrayList<Double> neighbors = new ArrayList<>(distanceMatrix.get(currentCity.getCityID()));
            Collections.sort(neighbors);
            for (int i = 1; i < neighbors.size(); i++) {
                City city = cities.get(distanceMatrix.get(currentCity.getCityID()).indexOf(neighbors.get(i)));
                if (!visitedCities.contains(city)){
                    currentCity = city;
                    break;
                }
            }
            visitedCities.add(currentCity);
        }
        visitedCities.add(startingCity);

        for (int i = 0; i < visitedCities.size() - 1; i++) {
            tripLength += distanceMatrix.get(visitedCities.get(i)
                    .getCityID()).get(visitedCities.get(i + 1).getCityID());
        }

    }

    @Override
    public int compareTo(Ant o) {
        return Double.compare(this.tripLength, o.tripLength);
    }

    public ArrayList<City> getVisitedCities() {
        return visitedCities;
    }

    public double getTripLength() {
        return tripLength;
    }

    @Override
    public String toString() {
        return "Ant{" +
                "tripLength=" + tripLength +
                '}';
    }
}


