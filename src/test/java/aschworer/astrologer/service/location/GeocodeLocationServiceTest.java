package aschworer.astrologer.service.location;

import aschworer.astrologer.alexa.service.locationservice.*;
import org.junit.*;

import static org.junit.Assert.*;


/**
 * @author ASC
 */
public class GeocodeLocationServiceTest {

    private GoogleLocationService locationService = new GoogleLocationService();

    @Test
    public void testSuccess() throws Exception {
        assertLocation(locationService.getFirstLocationByName("Australia"));
        assertLocation(locationService.getFirstLocationByName("Moscow"));
        assertLocation(locationService.getFirstLocationByName("Nadym"));
    }

    private void assertLocation(Location location) {
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLng().isEmpty());
        assertFalse(location.getFullName().isEmpty());
        assertFalse(location.getTimezoneOffset().isEmpty());
        assertFalse(location.getTimezoneName().isEmpty());
    }

    @Test
    public void testSuccess1() throws Exception {
        assertLocation(locationService.getFirstLocationByName("New Zealand"));
    }

    @Test
    public void testSuccess2() throws Exception {
        assertLocation(locationService.getFirstLocationByName("France"));
    }

    @Test
    public void testSuccess4() throws Exception {
        assertLocation(locationService.getFirstLocationByName("Russia"));
    }

    @Test
    public void testSuccess5() throws Exception {
        assertLocation(locationService.getFirstLocationByName("Russian Federation"));
    }

    @Test
    public void testSuccess6() throws Exception {
        assertLocation(locationService.getFirstLocationByName("India"));
    }

}
