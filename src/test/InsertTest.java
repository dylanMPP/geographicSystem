import exception.CountryNotFoundException;
import exception.WrittenFormatException;
import junit.framework.TestCase;
import model.*;
import java.util.ArrayList;

public class InsertTest  extends TestCase {

    private GeographySystemController controller;

    public void setupStage1() {
        controller = new GeographySystemController();
        controller.getCountryArrayList().add(new Country("0123", "Prueba Pais 0", 3.2, "+00"));
        controller.getCityArrayList().add(new City("321", "Prueba Ciudad 0", "0123", 0.102));
    }

    public void testInsertIntoCountries() throws WrittenFormatException, CountryNotFoundException {
        setupStage1();
        int previousSize = controller.getCountryArrayList().size();

        try {
            controller.callCommandMethod("INSERT INTO countries(id, name, population, countryCode) VALUES ('1111', 'Prueba País 1', 100.1, '+57')");
            controller.callCommandMethod("INSERT INTO countries(id, name, population, countryCode) VALUES ('2222', 'Prueba País 2', 221.234, '+32')");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        } catch (CountryNotFoundException countryNotFoundException){
            throw new CountryNotFoundException("The ID of the country isn't in the data base");
        }

        assertEquals(previousSize+2, controller.getCountryArrayList().size());

        controller.getCountryArrayList().remove(previousSize-1);
        controller.getCountryArrayList().remove(controller.getCountryArrayList().size()-2);
        controller.getCountryArrayList().remove(controller.getCountryArrayList().size()-1);
    }

    public void testInsertIntoCities() throws WrittenFormatException, CountryNotFoundException {
        setupStage1();
        int previousSize = controller.getCountryArrayList().size();

        try {
            controller.callCommandMethod("INSERT INTO cities(id, name, countryID, population) " +
                    "VALUES ('3333', 'Prueba Ciudad 1', '0123', 0.124)");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        } catch (CountryNotFoundException countryNotFoundException){
            throw new CountryNotFoundException("The ID of the country isn't in the data base");
        }

        assertEquals(previousSize+1, controller.getCityArrayList().size());

        // Ciudad que no pertenece a un country existente, el size es el mismo de antes
        try{
            controller.callCommandMethod("INSERT INTO cities(id, name, countryID, population) " +
                    "VALUES ('4444', 'Prueba Ciudad 2', '0000', 1.5)");
            fail();
        } catch (WrittenFormatException writtenFormatException) {
            assertEquals(previousSize+1, controller.getCityArrayList().size());
        } catch (CountryNotFoundException countryNotFoundException){
            assertEquals(previousSize+1, controller.getCityArrayList().size());
        }

        controller.getCityArrayList().remove(previousSize-1);
        controller.getCountryArrayList().remove(controller.getCountryArrayList().size()-1);
    }

    public void testInsertIntoCountries2() throws WrittenFormatException, CountryNotFoundException{
        setupStage1();
        int previousSize = controller.getCountryArrayList().size();

        try {
            controller.callCommandMethod("INSERT INTO countries(id, name, population, countryCode) VALUES ('0123', 'Prueba País 3', 115.9, '+41')");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        } catch (CountryNotFoundException countryNotFoundException){
            throw new CountryNotFoundException("The ID of the country isn't in the data base");
        }

        // No se modifica el size del country array list porque los ID son iguales
        assertEquals(previousSize, controller.getCountryArrayList().size());

        controller.getCountryArrayList().remove(previousSize-1);
    }

    public void testInsertIntoCities2() throws WrittenFormatException, CountryNotFoundException{
        setupStage1();
        int previousSize = controller.getCityArrayList().size();

        try {
            controller.callCommandMethod("INSERT INTO cities(id, name, countryID, population) " +
                    "VALUES ('321', 'Prueba Ciudad 2', '0123', 0.3)");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        } catch (CountryNotFoundException countryNotFoundException){
            throw new CountryNotFoundException("The ID of the country isn't in the data base");
        }

        // Sigue siendo previous size porque la city tiene ID repetido
        assertEquals(previousSize, controller.getCityArrayList().size());

        controller.getCityArrayList().remove(previousSize-1);
    }
}
