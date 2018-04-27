package aschworer.astrologer.service.location;

import aschworer.astrologer.alexa.service.GeocodeLocationService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author ASC
 */
public class GeocodeLocationServiceTest {

    private GeocodeLocationService locationService = new GeocodeLocationService();

    @Test
    public void testSuccess() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("Australia");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

    @Test
    public void testSuccess1() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("New Zealand");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

    @Test
    public void testSuccess2() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("France");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

    @Test
    public void testSuccess4() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("Russia");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

    @Test
    public void testSuccess5() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("Russian Federation");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

    @Test
    public void testSuccess6() throws Exception {
        final GeocodeLocationService.GeoLocation location = locationService.getFirstLocationByName("India");
        assertFalse(location.getLat().isEmpty());
        assertFalse(location.getLat().isEmpty());
    }

}
