import exception.CountryNotFoundException;
import exception.WrittenFormatException;
import junit.framework.TestCase;
import model.*;

import java.io.IOException;

public class DeleteTest extends TestCase {

    private GeographySystemController controller;

    public void setupStage1() {
        controller = new GeographySystemController();
        controller.getCountryArrayList().add(new Country("0123", "Prueba Pais 0", 3.2, "+00"));
        controller.getCityArrayList().add(new City("321", "Prueba Ciudad 0", "0123", 0.102));
    }

    public void testDeleteInCountries() throws WrittenFormatException, CountryNotFoundException, IOException {
        setupStage1();
        int previousSize = controller.getCountryArrayList().size();

        try {
            controller.callCommandMethod("DELETE FROM countries WHERE id = '0123'");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        }

        assertEquals(previousSize-1, controller.getCountryArrayList().size());
    }

    public void testDeleteCitiesFromColombia() throws WrittenFormatException, CountryNotFoundException, IOException {
        setupStage1();

        controller.getCountryArrayList().add(new Country("6ec3e8ec-3dd0-11ed-b878-0242ac120002", "Colombia", 45.9, "+56"));
        controller.getCityArrayList().add(new City("111", "Buga", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
        controller.getCityArrayList().add(new City("222", "Yumbo", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
        controller.getCityArrayList().add(new City("333", "Tuluá", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));

        int previousSizeCity = controller.getCityArrayList().size();
        int previousSizeCountry = controller.getCountryArrayList().size();

        try {
            controller.callCommandMethod("DELETE FROM cities WHERE country = 'Colombia'");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        }

        assertEquals(previousSizeCity-3, controller.getCityArrayList().size());
        assertEquals(previousSizeCountry, controller.getCountryArrayList().size());
    }
}
