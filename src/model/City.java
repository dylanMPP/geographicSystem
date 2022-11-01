package model;

public class City {

    private String id;
    private String name;
    private String countryId;
    private double populationInMillion;

    public City(String id, String name, String countryId, double populationInMillion){
        this.id = id;
        this.name = name;
        this.countryId = countryId;
        this.populationInMillion = populationInMillion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public double getPopulationInMillion() {
        return populationInMillion;
    }

    public void setPopulationInMillion(double populationInMillion) {
        this.populationInMillion = populationInMillion;
    }
}
