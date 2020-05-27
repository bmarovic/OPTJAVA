import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class City {

    private double xCoordinate;
    private double yCoordinate;
    private int cityID;
    private ArrayList<City> candidateList = new ArrayList<>();

    public City(double xCoordinate, double yCoordinate, int cityID) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.cityID = cityID;
    }

    public double cartesianDistance(City city){
        return Math.sqrt(Math.pow(this.xCoordinate - city.xCoordinate, 2)
                + Math.pow(this.yCoordinate - city.yCoordinate, 2));
    }

    public void calculateCandidateList(int k, ArrayList<ArrayList<Double>> distanceMatrix, ArrayList<City> cities){
        ArrayList<Double> row = new ArrayList<>(distanceMatrix.get(cityID));
        Collections.sort(row);
        for (int i = 1; i < k + 2; i++) {
            int index = distanceMatrix.get(cityID).indexOf(row.get(i));
            candidateList.add(cities.get(index));
        }
    }

    public ArrayList<City> getCandidateList() {
        return candidateList;
    }

    public int getCityID() {
        return cityID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return cityID == city.cityID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityID);
    }
}
