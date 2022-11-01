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
}
