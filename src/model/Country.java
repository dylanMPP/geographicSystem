package model;

import java.util.Collections;

public class Country {

    private String id;
    private String name;
    private double populationInMillion;
    private String phoneCode;

    public Country(String id, String name, double populationInMillion, String phoneCode){
        this.id = id;
        this.name = name;
        this.populationInMillion = populationInMillion;
        this.phoneCode = phoneCode;
    }

    // get and set
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

    public double getPopulationInMillion() {
        return populationInMillion;
    }

    public void setPopulationInMillion(double populationInMillion) {
        this.populationInMillion = populationInMillion;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }
}
