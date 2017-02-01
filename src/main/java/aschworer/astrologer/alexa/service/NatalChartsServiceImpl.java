package aschworer.astrologer.alexa.service;

import aschworer.astrologer.alexa.handler.responder.service.NatalChartsService;
import aschworer.astrologer.alexa.service.model.GeoLocation;
import aschworer.astrologer.alexa.service.model.NatalChart;
import aschworer.astrologer.alexa.service.model.Sign;
import aschworer.astrologer.alexa.service.natalcharts.SwissEphemerisLambda;
import aschworer.astrologer.alexa.service.location.GoogleLocationService;
import aschworer.astrologer.alexa.service.model.BirthDetails;
import aschworer.astrologer.alexa.service.model.Planet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author aschworer
 */
public class NatalChartsServiceImpl implements NatalChartsService {

    private static final Logger logger = LoggerFactory.getLogger(NatalChartsServiceImpl.class);

    private GoogleLocationService locationService = new GoogleLocationService();
    private SwissEphemerisLambda swissEphLambdaFunction = new SwissEphemerisLambda();

    @Override
    public Sign getPlanetSign(Planet planet, Date date) throws Exception {
        return getNatalChartByDate(date).getSign(planet);
    }

    public NatalChart getNatalChartByDate(Date date) throws Exception {
        BirthDetails birthDetails = getDefaultBirthDetails();
        birthDetails.setDate(SwissEphemerisLambda.DATE_FORMAT.format(date));
        birthDetails.setTime(SwissEphemerisLambda.TIME_FORMAT.format(date));//todo timezone of birth place
        return getNatalChart(birthDetails);
    }

    public NatalChart getNatalChartByDateAndPlace(Date date, String place) throws Exception {
        BirthDetails birthDetails = getDefaultBirthDetails();
        birthDetails.setDate(SwissEphemerisLambda.DATE_FORMAT.format(date));
        birthDetails.setTime(SwissEphemerisLambda.TIME_FORMAT.format(date));//todo timezone of birth place
        GeoLocation geoLocation = locationService.getFirstLocationByName(place);
        birthDetails.setLng(geoLocation.getLng());
        birthDetails.setLat(geoLocation.getLat());
        return getNatalChart(birthDetails);
    }

    private BirthDetails getDefaultBirthDetails() {
        BirthDetails birthDetails = new BirthDetails();
//        birthDetails.setTime("00:00");//todo -
        birthDetails.setTimezone("+00:00");
        birthDetails.setLat("00:00");//todo - fix this on lambda side
        birthDetails.setLng("00:00");
        return birthDetails;
    }

    private NatalChart getNatalChart(BirthDetails birthDetails) throws /*BirthTimeRequiredException, *//*LocationRequiredException, */NatalChartRetrievalException {
        //todo try getting chart with different dates/locations - if not same - throw location/time required
        try {
            logger.debug(birthDetails.toString());
            return swissEphLambdaFunction.getNatalChart(birthDetails);
        } catch (Exception e) {
            throw new NatalChartRetrievalException();
        }
    }
}
