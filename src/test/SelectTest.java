import exception.CountryNotFoundException;
import exception.WrittenFormatException;
import junit.framework.Test;
import junit.framework.TestCase;
import model.City;
import model.Country;
import model.GeographySystemController;

import java.io.IOException;

public class SelectTest extends TestCase {

    private GeographySystemController controller;

    public void setupStage1() {
            controller = new GeographySystemController();
            controller.getCountryArrayList().add(new Country("0123", "Prueba Pais 0", 77, "+00"));
            controller.getCityArrayList().add(new City("321", "Prueba Ciudad 0", "0123", 0.102));
    }

    public void testSelectAllCitiesFromColombia() throws WrittenFormatException, CountryNotFoundException, IOException {
            setupStage1();

            controller.getCountryArrayList().add(new Country("6ec3e8ec-3dd0-11ed-b878-0242ac120002", "Colombia", 45.9, "+56"));
            controller.getCityArrayList().add(new City("111", "Buga", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
            controller.getCityArrayList().add(new City("222", "Yumbo", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
            controller.getCityArrayList().add(new City("333", "Tuluá", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
            controller.getCityArrayList().add(new City("0924", "No pertenece", "2", 0.102));

            int previousSizeCountry = controller.getCountryArrayList().size();
            int previousSizeCity = controller.getCityArrayList().size();
            String list = "";

            // Hago la prueba imprimiendo la lista en consola y verificando que el mensaje de la lista es distinto a ""
            // Igualmente, agregué ciudades de Colombia a la base de datos para que sea probado por la interfaz gráfica
            try {
                list = controller.callCommandMethod("SELECT * FROM cities WHERE country = 'Colombia'");
            } catch (WrittenFormatException writtenFormatException) {
                throw new WrittenFormatException("Written format isn't correct");
            }

            System.out.println(list);
            assertNotSame("", list);
            assertEquals(previousSizeCity, controller.getCityArrayList().size());
            assertEquals(previousSizeCountry, controller.getCountryArrayList().size());
    }


    public void testSelectAllCitiesFromColombia2() throws WrittenFormatException, CountryNotFoundException, IOException {
        setupStage1();

        controller.getCountryArrayList().add(new Country("6ec3e8ec-3dd0-11ed-b878-0242ac120002", "Colombia", 45.9, "+56"));
        controller.getCityArrayList().add(new City("111", "Buga", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
        controller.getCityArrayList().add(new City("222", "Yumbo", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
        controller.getCityArrayList().add(new City("333", "Tuluá", "6ec3e8ec-3dd0-11ed-b878-0242ac120002", 0.102));
        controller.getCityArrayList().add(new City("0924", "No pertenece", "2", 0.102));

        int previousSizeCountry = controller.getCountryArrayList().size();
        int previousSizeCity = controller.getCityArrayList().size();
        String list = "";

        // Hago la prueba imprimiendo la lista en consola y verificando que el mensaje de la lista es distinto a ""
        // Igualmente, agregué ciudades de Colombia a la base de datos para que sea probado por la interfaz gráfica
        try {
            list = controller.callCommandMethod("SELECT * FROM countries WHERE population < 46");
        } catch (WrittenFormatException writtenFormatException) {
            throw new WrittenFormatException("Written format isn't correct");
        }

        System.out.println(list);
        assertNotSame("", list);
        assertEquals(previousSizeCity, controller.getCityArrayList().size());
        assertEquals(previousSizeCountry, controller.getCountryArrayList().size());
    }
}
