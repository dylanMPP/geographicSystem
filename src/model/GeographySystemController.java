package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import exception.CountryNotFoundException;
import exception.WrittenFormatException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.text.CollationElementIterator;
import java.util.*;

public class GeographySystemController {

    private ArrayList<Country> countryArrayList;
    private ArrayList<City> cityArrayList;

    public GeographySystemController(){
        this.countryArrayList = new ArrayList<>();
        this.cityArrayList = new ArrayList<>();
    }

    public String generateUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public String callCommandMethod(String command) throws WrittenFormatException, CountryNotFoundException {

        String type = commandType(command);

        if (type.equalsIgnoreCase("INSERT INTO countries")) {

            if(insertInto(command, "countries")){
                return "The command was executed successfully.";
            } else {
                return "";
            }

        } else if (type.equalsIgnoreCase("INSERT INTO cities")) {

            if(insertInto(command, "cities")){
                return "The command was executed successfully.";
            } else {
                return "";
            }

        } else if (type.equalsIgnoreCase("DELETE FROM countries WHERE")) {
            if(delete(command, "countries")){
                return "The command was executed successfully.";
            } else {
                return "";
            }

        } else if (type.equalsIgnoreCase("DELETE FROM cities WHERE")) {
            if(delete(command, "cities")){
                return "The command was executed successfully.";
            } else {
                return "";
            }

        } else if (type.equalsIgnoreCase("SELECT * FROM countries WHERE")) {
            return select(command, "WHERE", "countries");

        } else if (type.equalsIgnoreCase("SELECT * FROM cities WHERE")) {
            return select(command, "WHERE", "cities");

        } else if (type.equalsIgnoreCase("SELECT * FROM countries WHERE ORDER BY")) {
            return select(command, "ORDER BY", "countries");

        } else if (type.equalsIgnoreCase("SELECT * FROM cities WHERE ORDER BY")) {
            return select(command, "ORDER BY", "cities");

        } else if (type.equalsIgnoreCase("SELECT * FROM cities ORDER BY")){
            return select(command, "ONLY ORDER BY", "cities");

        } else if(type.equalsIgnoreCase("SELECT * FROM countries ORDER BY")){
            return select(command, "ONLY ORDER BY", "countries");

        } else if (type.equalsIgnoreCase("SELECT * FROM countries")) {
            return select(command, "*", "countries");

        } else if (type.equalsIgnoreCase("SELECT * FROM cities")){
            return select(command, "*", "cities");

        } else {
            throw new WrittenFormatException("Written format isn't correct");
        }
    }

    public String commandType(String command) throws WrittenFormatException {
        String[] parts = command.split("");
        int amountOfBrackets = 0;
        int amountOfCommas = 0;

        if(command.contains("SELECT * FROM cities ORDER BY") && !command.contains("WHERE")){
            return "SELECT * FROM cities ORDER BY";


        } else if(command.contains("SELECT * FROM countries ORDER BY") && !command.contains("WHERE")) {
            return "SELECT * FROM countries ORDER BY";
        }

        for (int i = 0; i < parts.length; i++) {
            if(parts[i].equalsIgnoreCase("(") || parts[i].equalsIgnoreCase(")")){
                amountOfBrackets++;
            }
            if(parts[i].equalsIgnoreCase(",")){
                amountOfCommas++;
            }
        }

        if(command.contains("INSERT INTO countries(id, name, population, countryCode) VALUES")) {
            if(amountOfBrackets!=4 || amountOfCommas!=6){
                throw new WrittenFormatException("Written format isn't correct");
            } else {
                return "INSERT INTO countries";
            }

        } else if(command.contains("INSERT INTO cities(id, name, countryID, population) VALUES")){
            if(amountOfBrackets!=4 || amountOfCommas!=6){
                throw new WrittenFormatException("Written format isn't correct");
            } else {
                return "INSERT INTO cities";
            }

        } else if(command.contains("DELETE FROM countries WHERE")) {
            if(command.contains("name") || command.contains("id") || command.contains("countryCode")){
                if(!parts[parts.length-1].equalsIgnoreCase("'")){
                    throw new WrittenFormatException("Written format isn't correct");
                } else {
                    return "DELETE FROM countries WHERE";
                }
            } else {
                return "DELETE FROM countries WHERE";
            }

        } else if(command.contains("DELETE FROM cities WHERE")) {
            if(command.contains("name") || command.contains("id") || command.contains("countryID")){
                if(!parts[parts.length-1].equalsIgnoreCase("'")){
                    throw new WrittenFormatException("Written format isn't correct");
                } else {
                    return "DELETE FROM cities WHERE";
                }
            } else {
                return "DELETE FROM cities WHERE";
            }

        } else if(command.contains("SELECT * FROM countries WHERE") && !command.contains("ORDER BY")){
            if(command.contains("name") || command.contains("id") || command.contains("countryCode")){
                if(!parts[parts.length-1].equalsIgnoreCase("'")){
                    throw new WrittenFormatException("Written format isn't correct");
                } else {
                    return "SELECT * FROM countries WHERE";
                }
            } else {
                return "SELECT * FROM countries WHERE";
            }

        } else if(command.contains("SELECT * FROM cities WHERE") && !command.contains("ORDER BY")) {
            if(command.contains("name") || command.contains("id") || command.contains("countryID")){
                if(!parts[parts.length-1].equalsIgnoreCase("'")){
                    throw new WrittenFormatException("Written format isn't correct");
                } else {
                    return "SELECT * FROM cities WHERE";
                }
            } else {
                return "SELECT * FROM cities WHERE";
            }

        } else if(command.contains("SELECT * FROM countries WHERE") && command.contains("ORDER BY")){
            return "SELECT * FROM countries WHERE ORDER BY";

        } else if(command.contains("SELECT * FROM cities WHERE") && command.contains("ORDER BY")) {
            return "SELECT * FROM cities WHERE ORDER BY";

        } else if(command.contains("SELECT * FROM countries")){
            return "SELECT * FROM countries";

        } else if(command.contains("SELECT * FROM cities")) {
            return "SELECT * FROM cities";

        } else {
            throw new WrittenFormatException("Written format isn't correct");
        }
    } // command type method

    public String select(String command, String condition, String where) throws WrittenFormatException
    {
        String[] parts;
        String[] leftParts;
        String[] rightParts;
        String attribute = "";
        String conditionR = "";
        String orderBy = "";
        String msg = "";
        String sign = "";

        if(command.contains("<=") || command.contains(">=") || command.contains("<<") || command.contains(">>") || command.contains("<>") ||
                command.contains("><") || command.contains("=>") || command.contains("=<")){
            throw new WrittenFormatException("Written format isn't correct");
        }

        if(command.contains("=")){
            sign = "=";
            parts = command.split("=");
        } else if(command.contains("<")){
            sign = "<";
            parts = command.split("<");
        } else if(command.contains(">")){
            sign = ">";
            parts = command.split(">");
        } else {
            parts = command.split(" ");
        }

        if(condition.equalsIgnoreCase("ONLY ORDER BY")){
            if(where.equalsIgnoreCase("countries")){
                String[] partsOnlyOrderBy = command.split(" ");
                // SELECT * FROM cities ORDER BY name
                if(partsOnlyOrderBy.length!=7){
                    throw new WrittenFormatException("Written format isn't correct");
                }

                String conditionToOrder = partsOnlyOrderBy[6];

                if(conditionToOrder.equalsIgnoreCase("id")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    ArrayList<Country> orderByList = orderByCountry(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    ArrayList<Country> orderByList = orderByCountry(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    ArrayList<Country> orderByList = orderByCountry(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("countryCode")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    ArrayList<Country> orderByList = orderByCountry(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }

            } else if(where.equalsIgnoreCase("cities")){
                String[] partsOnlyOrderBy = command.split(" ");
                // SELECT * FROM cities ORDER BY name
                if(partsOnlyOrderBy.length!=7){

                    throw new WrittenFormatException("Written format isn't correct");
                }

                String conditionToOrder = partsOnlyOrderBy[6];

                if(conditionToOrder.equalsIgnoreCase("id")){
                    City[] unsorted = new City[cityArrayList.size()];
                    unsorted = cityArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    ArrayList<City> orderByList = orderByCity(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                    }

                    cityArrayList = new ArrayList<>();
                    Collections.addAll(cityArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    City[] unsorted = new City[cityArrayList.size()];
                    unsorted = cityArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    ArrayList<City> orderByList = orderByCity(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                    }

                    cityArrayList = new ArrayList<>();
                    Collections.addAll(cityArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    City[] unsorted = new City[cityArrayList.size()];
                    unsorted = cityArrayList.toArray(unsorted);

                    ArrayList<City> orderByList = orderByCity(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                    }

                    cityArrayList = new ArrayList<>();
                    Collections.addAll(cityArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("countryID")){
                    City[] unsorted = new City[cityArrayList.size()];
                    unsorted = cityArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    ArrayList<City> orderByList = orderByCity(conditionToOrder);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                    }

                    cityArrayList = new ArrayList<>();
                    Collections.addAll(cityArrayList, unsorted);
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }

            } else {
                throw new WrittenFormatException("Written format isn't correct");
            }
        }


        if(condition.equalsIgnoreCase("*")){
            if(parts.length!=4){
                throw new WrittenFormatException("Written format isn't correct");
            }
        } else if(condition.equalsIgnoreCase("WHERE")){
            if(parts.length!=2) {
                throw new WrittenFormatException("Written format isn't correct");
            }

            leftParts = parts[0].split(" ");
            attribute = leftParts[5];
            conditionR = parts[1];

        } else if(condition.equalsIgnoreCase("ORDER BY")) {
            if(parts.length!=2){
                throw new WrittenFormatException("Written format isn't correct");
            }
            leftParts = parts[0].split(" ");
            attribute = leftParts[5];
            rightParts = parts[1].split("ORDER BY");
            if(rightParts.length!=2){
                throw new WrittenFormatException("Written format isn't correct");
            }
            conditionR = rightParts[0];
            orderBy = rightParts[1];
        }

        if(where.equalsIgnoreCase("countries")){
            if(condition.equalsIgnoreCase("*")){
                for (Country country:
                        countryArrayList) {
                    msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                }
                return msg;

            } else if(condition.equalsIgnoreCase("WHERE")) {
                if (attribute.equalsIgnoreCase("id")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    for (Country country:
                            countryArrayList) {
                        if(country.getId().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    for (Country country:
                            countryArrayList) {
                        if(country.getName().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    String[] conditionR2 = conditionR.split(" ");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }
                    double conditionR3 = Double.parseDouble(conditionR2[1]);

                    if(sign.equalsIgnoreCase("=")){
                        for (Country country:
                                countryArrayList) {
                            if(country.getPopulationInMillion()==conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    } else if(sign.equalsIgnoreCase("<")){
                        for (Country country:
                                countryArrayList) {
                            if(country.getPopulationInMillion()<conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    } else {
                        for (Country country:
                                countryArrayList) {
                            if(country.getPopulationInMillion()>conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("countryCode")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    for (Country country:
                            countryArrayList) {
                        if(country.getPhoneCode().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }

            } else if(condition.equalsIgnoreCase("ORDER BY")) {

                if(attribute.equalsIgnoreCase("id")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<Country> orderByList = orderByCountry(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        if(country.getId().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    System.out.println(conditionR2.length+" "+orderBy2.length);
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<Country> orderByList = orderByCountry(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        if(country.getName().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }
                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    String[] conditionR2 = conditionR.split(" ");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=2 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    double conditionR3 = Double.parseDouble(conditionR2[1]);
                    String orderBy3 = orderBy2[1];
                    ArrayList<Country> orderByList = orderByCountry(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    if(sign.equalsIgnoreCase("=")){
                        for (Country country:
                                orderByList) {
                            if(country.getPopulationInMillion()==conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    } else if(sign.equalsIgnoreCase("<")){
                        for (Country country:
                                orderByList) {
                            if(country.getPopulationInMillion()<conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    } else {
                        for (Country country:
                                orderByList) {
                            if(country.getPopulationInMillion()>conditionR3){
                                msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                            }
                        }
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("countryCode")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<Country> orderByList = orderByCountry(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (Country country:
                            orderByList) {
                        if(country.getPhoneCode().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+country.getName()+" | ID: "+country.getId()+" | POPULATION: "+country.getPopulationInMillion()+" | PHONE CODE: "+country.getPhoneCode()+"\n";
                        }
                    }

                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }
            } // country conditions
            //  //  //  //  //  //
        } else if(where.equalsIgnoreCase("cities")){

            if(condition.equalsIgnoreCase("*")){
                for (City city:
                        cityArrayList) {
                    msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                }
                return msg;

            } else if(condition.equalsIgnoreCase("WHERE")){

                if (attribute.equalsIgnoreCase("id")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    for (City city:
                            cityArrayList) {
                        if(city.getId().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    for (City city:
                            cityArrayList) {
                        if(city.getName().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    String[] conditionR2 = conditionR.split(" ");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }
                    double conditionR3 = Double.parseDouble(conditionR2[1]);

                    if(sign.equalsIgnoreCase("=")){
                        for (City city:
                                cityArrayList) {
                            if(city.getPopulationInMillion()==conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    } else if(sign.equalsIgnoreCase("<")){
                        for (City city:
                                cityArrayList) {
                            if(city.getPopulationInMillion()<conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    } else {
                        for (City city:
                                cityArrayList) {
                            if(city.getPopulationInMillion()>conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    }
                    return msg;

                } else if(attribute.equalsIgnoreCase("country")){
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    if(conditionR2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];

                    String countryId = "";

                    for (int i=0; i<countryArrayList.size(); i++) {
                        if(countryArrayList.get(i).getName().equalsIgnoreCase(conditionR3)){
                            countryId = countryArrayList.get(i).getId();
                            break;
                        }
                    } // for each

                    for (City city:
                            cityArrayList) {
                        if(city.getCountryId().equalsIgnoreCase(countryId)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }

            } else if(condition.equalsIgnoreCase("ORDER BY")) {

                if(attribute.equalsIgnoreCase("id")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<City> orderByList = orderByCity(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        if(city.getId().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("name")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<City> orderByList = orderByCity(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    for (City city:
                            orderByList) {
                        if(city.getName().equalsIgnoreCase(conditionR3)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("population")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);
                    String[] conditionR2 = conditionR.split(" ");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=2 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    double conditionR3 = Double.parseDouble(conditionR2[1]);
                    String orderBy3 = orderBy2[1];

                    ArrayList<City> orderByList = orderByCity(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    if(sign.equalsIgnoreCase("=")){
                        for (City city:
                                orderByList) {
                            if(city.getPopulationInMillion()==conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    } else if(sign.equalsIgnoreCase("<")){
                        for (City city:
                                orderByList) {
                            if(city.getPopulationInMillion()<conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    } else {
                        for (City city:
                                orderByList) {
                            if(city.getPopulationInMillion()>conditionR3){
                                msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                            }
                        }
                    }
                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else if(attribute.equalsIgnoreCase("country")){
                    Country[] unsorted = new Country[countryArrayList.size()];
                    unsorted = countryArrayList.toArray(unsorted);

                    if(!sign.equalsIgnoreCase("=")){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String[] conditionR2 = conditionR.split("'");
                    String[] orderBy2 = orderBy.split(" ");
                    if(conditionR2.length!=3 || orderBy2.length!=2){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String conditionR3 = conditionR2[1];
                    String orderBy3 = orderBy2[1];

                    ArrayList<City> orderByList = orderByCity(orderBy3);
                    if(orderByList==null){
                        throw new WrittenFormatException("Written format isn't correct");
                    }

                    String countryId = "";

                    for (int i=0; i<countryArrayList.size(); i++) {
                        if(countryArrayList.get(i).getName().equalsIgnoreCase(conditionR3)){
                            countryId = countryArrayList.get(i).getId();
                            break;
                        }
                    } // for each

                    for (City city:
                            orderByList) {
                        if(city.getCountryId().equalsIgnoreCase(countryId)){
                            msg += "- NAME: "+city.getName()+" | ID: "+city.getId()+" | POPULATION: "+city.getPopulationInMillion()+" | COUNTRY ID: "+city.getCountryId()+"\n";
                        }
                    }
                    countryArrayList = new ArrayList<>();
                    Collections.addAll(countryArrayList, unsorted);
                    return msg;

                } else {
                    throw new WrittenFormatException("Written format isn't correct");
                }
            } // city conditions
        } // countries or cities
        return "";
    }

    public boolean insertInto(String command, String where) throws WrittenFormatException, CountryNotFoundException {
        String[] parts = command.split("\\(");

        if(parts.length!=3){
            throw new WrittenFormatException("Written format isn't correct");
        }

        String[] valueParts = parts[2].split("'");

        if(valueParts.length!=7){
            throw new WrittenFormatException("Written format isn't correct");
        }

        String id = valueParts[1];
        String name = valueParts[3];

        if(where.equalsIgnoreCase("countries")){
            if(idIsInDataBase(id, "countries")){
                return false;
            }

            String[] populationParts = valueParts[4].split(",");

            if(populationParts.length!=3){
                throw new WrittenFormatException("Written format isn't correct");
            }

            String[] populationParts2 = populationParts[1].split(" ");

            if(populationParts2.length!=2){
                throw new WrittenFormatException("Written format isn't correct");
            }

            double populationInMillion = Double.parseDouble(populationParts2[1]);
            String phoneCode = valueParts[5];

            countryArrayList.add(new Country(id, name, populationInMillion, phoneCode));
            return true;

        } else if(where.equalsIgnoreCase("cities")){
            if(idIsInDataBase(id, "cities")){
                return false;
            }

            String[] populationParts = valueParts[6].split(" ");
            String[] populationParts2 = populationParts[1].split("\\)");

            if(populationParts.length!=2 || populationParts2.length!=1){
                throw new WrittenFormatException("Written format isn't correct");
            }

            String countryID = valueParts[5];
            double populationInMillion = Double.parseDouble(populationParts2[0]);

            boolean countryExists = false;

            for (Country country: countryArrayList) {
                if(country.getId().equalsIgnoreCase(countryID)){
                    countryExists = true;
                    break;
                }
            }

            if(!countryExists){
                throw new CountryNotFoundException("The ID of the country isn't in the data base");
            }

            cityArrayList.add(new City(id, name, countryID, populationInMillion));
            return true;
        } else {
            throw new WrittenFormatException("Written format isn't correct");
        }
    } // insert into method

    public boolean delete(String command, String where) throws WrittenFormatException {
        if(command.equalsIgnoreCase("DELETE * FROM countries")){
            if(!countryArrayList.isEmpty()){
                countryArrayList.clear();
            }
        } else if(command.equalsIgnoreCase("DELETE * FROM cities")){
            if(!cityArrayList.isEmpty()){
                cityArrayList.clear();
            }
        }

        boolean major = false;
        boolean majorEquals = false;
        boolean equals = false;
        boolean minorEquals = false;
        boolean minor = false;

        String[] deleteParts = command.split(" ");

        if(deleteParts.length!=7){
            throw new WrittenFormatException("Written format isn't correct");
        }

        String attribute = deleteParts[4];

        String[] conditionParts = deleteParts[6].split("'");
        if(conditionParts.length!=2){
            return false;
        }
        String condition = conditionParts[1];

        if(command.contains(">")){
            major = true;
        } else if(command.contains(">=")){
            majorEquals = true;
        } else if(command.contains("<")){
            minor = true;
        } else if(command.contains("<=")){
            minorEquals = true;
        } else if(command.contains("=")){
            equals = true;
        } else {
            throw new WrittenFormatException("Written format isn't correct");
        }

        if(where.equalsIgnoreCase("countries")){
            if(attribute.equalsIgnoreCase("name")) {
                boolean removed = false;

                for (int i=countryArrayList.size()-1; i>=0; i--) {
                    if (countryArrayList.get(i).getName().equalsIgnoreCase(condition)) {
                        countryArrayList.remove(i);
                        removed = true;
                    }
                } // for each

                return removed;
            } else if(attribute.equalsIgnoreCase("id")){
                boolean removed = false;

                for (int i=countryArrayList.size()-1; i>=0; i--) {
                    if (countryArrayList.get(i).getId().equalsIgnoreCase(condition)) {
                        countryArrayList.remove(i);
                        removed = true;
                    }
                } // for each

                return removed;
            } else if(attribute.equalsIgnoreCase("population")){
                boolean removed = false;

                for (int i=countryArrayList.size()-1; i>=0; i--) {

                    if(major){
                        if(countryArrayList.get(i).getPopulationInMillion()>Double.parseDouble(condition)){
                            countryArrayList.remove(i);
                            removed = true;
                        }
                    } else if(majorEquals){
                        if(countryArrayList.get(i).getPopulationInMillion()>=Double.parseDouble(condition)){
                            countryArrayList.remove(i);
                            removed = true;
                        }
                    } else if(minor){
                        if(countryArrayList.get(i).getPopulationInMillion()<Double.parseDouble(condition)){
                            countryArrayList.remove(i);
                            removed = true;
                        }
                    } else if(minorEquals){
                        if(countryArrayList.get(i).getPopulationInMillion()<=Double.parseDouble(condition)){
                            countryArrayList.remove(i);
                            removed = true;
                        }
                    } else if(equals){
                        if(countryArrayList.get(i).getPopulationInMillion()==Double.parseDouble(condition)){
                            countryArrayList.remove(i);
                            removed = true;
                        }
                    }
                } // for each
                return removed;
            } else if(attribute.equalsIgnoreCase("countryCode")){
                boolean removed = false;

                for (int i=countryArrayList.size()-1; i>=0; i--){
                    if(countryArrayList.get(i).getPhoneCode().equalsIgnoreCase(condition)){
                        countryArrayList.remove(i);
                        removed = true;
                    }
                }
                return removed;
            } else {
                throw new WrittenFormatException("Written format isn't correct");
            }
        } else if(where.equalsIgnoreCase("cities")){
            if(attribute.equalsIgnoreCase("name")) {
                boolean removed = false;

                for (int i=cityArrayList.size()-1; i>=0; i--) {
                    if (cityArrayList.get(i).getName().equalsIgnoreCase(condition)) {
                        cityArrayList.remove(i);
                        removed = true;
                    }
                } // for
                return removed;
            } else if(attribute.equalsIgnoreCase("id")){
                boolean removed = false;

                for (int i=cityArrayList.size()-1; i>=0; i--) {
                    if (cityArrayList.get(i).getId().equalsIgnoreCase(condition)) {
                        cityArrayList.remove(i);
                        removed = true;
                    }
                } // for
                return removed;
            } else if(attribute.equalsIgnoreCase("country")) {
                boolean removed = false;
                String countryId = "";

                for (int i=0; i<countryArrayList.size(); i++) {
                    if(countryArrayList.get(i).getName().equalsIgnoreCase(condition)){
                        countryId = countryArrayList.get(i).getId();
                        break;
                    }
                } // for each
                System.out.println(countryId);
                for (int i=cityArrayList.size()-1; i>=0; i--) {
                    if(cityArrayList.get(i).getCountryId().equalsIgnoreCase(countryId)){
                        cityArrayList.remove(i);
                        removed = true;
                    }
                } // for each
                return removed;

            } else if(attribute.equalsIgnoreCase("population")){
                boolean removed = false;

                for (int i=cityArrayList.size()-1; i>=0; i--) {

                    if(major){
                        if(cityArrayList.get(i).getPopulationInMillion()>Double.parseDouble(condition)){
                            cityArrayList.remove(i);
                            removed = true;
                        }
                    } else if(majorEquals){
                        if(cityArrayList.get(i).getPopulationInMillion()>=Double.parseDouble(condition)){
                            cityArrayList.remove(i);
                            removed = true;
                        }
                    } else if(minor){
                        if(cityArrayList.get(i).getPopulationInMillion()<Double.parseDouble(condition)){
                            cityArrayList.remove(i);
                            removed = true;
                        }
                    } else if(minorEquals){
                        if(cityArrayList.get(i).getPopulationInMillion()<=Double.parseDouble(condition)){
                            cityArrayList.remove(i);
                            removed = true;
                        }
                    } else if(equals){
                        if(cityArrayList.get(i).getPopulationInMillion()==Double.parseDouble(condition)){
                            cityArrayList.remove(i);
                            removed = true;
                        }
                    }
                } // for each
                return removed;

            } else {
                throw new WrittenFormatException("Written format isn't correct");
            }
        } else {
            throw new WrittenFormatException("Written format isn't correct");
        }
    } // delete

    public boolean idIsInDataBase(String id, String where){
        if(where.equalsIgnoreCase("countries")){
            for (Country country:
                 countryArrayList) {
                if(country.getId().equalsIgnoreCase(id)){
                    return true;
                }
            } // for each

        } else if(where.equalsIgnoreCase("cities")){
            for (City city:
                 cityArrayList) {
                if(city.getId().equalsIgnoreCase(id)){
                    return true;
                }
            }
        }
        return false;
    } // ID is in data base

    public void save() throws FileNotFoundException, IOException {
        Gson gson = new Gson();
        String jsonCountries = gson.toJson(this.countryArrayList);
        String jsonCities = gson.toJson(this.cityArrayList);
        String json = jsonCountries+jsonCities;
        File file = new File("data.json");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();
            return;
        } catch (FileNotFoundException fileNotFoundException){
            throw new FileNotFoundException("The file wasn't found");
        } catch (IOException ioException){
            throw new IOException("IOE exception found");
        }
    }

    public void load() throws FileNotFoundException, IOException {
        File file = new File("data.json");
        file.exists();

        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            String json = "";

            if((line = reader.readLine())!=null){
                json = line;
            }
            System.out.println(line);
            fis.close();

            JsonReader jsonReader = new JsonReader(new StringReader(json));
            jsonReader.setLenient(true);

            Gson gson = new Gson();
            Country[] countriesFromJson = gson.fromJson(jsonReader, Country[].class);
            City[] citiesFromJson = gson.fromJson(jsonReader, City[].class);

            for (Country country:
                    countriesFromJson) {
                this.countryArrayList.add(country);
            }

            for (City city:
                    citiesFromJson) {
                this.cityArrayList.add(city);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileNotFoundException("File not found");
        } catch (IOException ioException){
            throw new IOException("IOE exception found");
        }
    }

    public String importFromSQL(String file) throws WrittenFormatException, CountryNotFoundException, FileNotFoundException, IOException{
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            String imported = "";

            while((line = reader.readLine())!=null){
                imported = line;
                System.out.println(imported);
                try {
                    if(callCommandMethod(imported).equalsIgnoreCase("")){
                        return "";
                    }
                } catch (WrittenFormatException writtenFormatException){
                    throw new WrittenFormatException("Written format isn't correct");
                } catch (CountryNotFoundException countryNotFoundException){
                    throw new CountryNotFoundException("The country wasn't found");
                }
            }
            fis.close();
            return "imported";
        } catch (FileNotFoundException fileNotFoundException) {
            throw new FileNotFoundException("File not found");
        } catch (IOException ioException){
            throw new IOException("IOE exception found");
        }
    }

    private ArrayList<Country> orderByCountry(String orderBy3) {

        if(orderBy3.equalsIgnoreCase("id")){
            return sortByIDCountry();
        } else if(orderBy3.equalsIgnoreCase("name")){
            return sortByNameCountry();
        } else if(orderBy3.equalsIgnoreCase("population")){
            return sortByPopulationCountry();
        } else if(orderBy3.equalsIgnoreCase("countryCode")){
            return sortByPhoneCodeCountry();
        } else {
            return null;
        }
    }

    private ArrayList<City> orderByCity(String orderBy3) {

        if(orderBy3.equalsIgnoreCase("id")){
            return sortByIDCity();
        } else if(orderBy3.equalsIgnoreCase("name")){
            return sortByNameCity();
        } else if(orderBy3.equalsIgnoreCase("population")){
            return sortByPopulationCity();
        } else if(orderBy3.equalsIgnoreCase("countryCode")){
            return sortByCountryIDCity();
        } else {
            return null;
        }
    }

    // Sorting
    public ArrayList<Country> sortByIDCountry(){

        ArrayList<Country> auxiliar = countryArrayList;

        auxiliar.sort(new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getId().compareTo(country2.getId());
            }
        });

        return auxiliar;
    } // sort by name country

    public ArrayList<Country> sortByNameCountry(){
        ArrayList<Country> auxiliar = countryArrayList;

        auxiliar.sort(new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareTo(country2.getName());
            }
        });

        return auxiliar;
    } // sort by name country

    public ArrayList<Country> sortByPopulationCountry(){
        ArrayList<Country> auxiliar = countryArrayList;

        auxiliar.sort(new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                if(country1.getPopulationInMillion()>country2.getPopulationInMillion()){
                    return 1;
                } else if(country1.getPopulationInMillion()<country2.getPopulationInMillion()){
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return auxiliar;
    } // sort by population country

    public ArrayList<Country> sortByPhoneCodeCountry(){
        ArrayList<Country> auxiliar = countryArrayList;

        auxiliar.sort(new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getPhoneCode().compareTo(country2.getPhoneCode());
            }
        });

        return auxiliar;
    } // sort by phone code country

    public ArrayList<City> sortByIDCity(){
        ArrayList<City> auxiliar = cityArrayList;

        auxiliar.sort(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                return city1.getId().compareTo(city2.getId());
            }
        });

        return auxiliar;
    } // sort by name city

    public ArrayList<City> sortByNameCity(){
        ArrayList<City> auxiliar = cityArrayList;

        auxiliar.sort(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                return city1.getName().compareTo(city2.getName());
            }
        });

        return auxiliar;
    } // sort by name city

    public ArrayList<City> sortByCountryIDCity(){
        ArrayList<City> auxiliar = cityArrayList;

        auxiliar.sort(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                return city1.getCountryId().compareTo(city2.getCountryId());
            }
        });

        return auxiliar;
    } // sort by name city

    public ArrayList<City> sortByPopulationCity(){
        ArrayList<City> auxiliar = cityArrayList;

        auxiliar.sort(new Comparator<City>() {
            @Override
            public int compare(City city1, City city2) {
                if(city1.getPopulationInMillion()>city2.getPopulationInMillion()){
                    return 1;
                } else if(city1.getPopulationInMillion()<city2.getPopulationInMillion()){
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return auxiliar;
    } // sort by population city

    // Getter and Setter
    public ArrayList<Country> getCountryArrayList() {
        return countryArrayList;
    }

    public void setCountryArrayList(ArrayList<Country> countryArrayList) {
        this.countryArrayList = countryArrayList;
    }

    public ArrayList<City> getCityArrayList() {
        return cityArrayList;
    }

    public void setCityArrayList(ArrayList<City> cityArrayList) {
        this.cityArrayList = cityArrayList;
    }
}
