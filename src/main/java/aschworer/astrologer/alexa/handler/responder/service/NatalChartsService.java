package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.service.model.GeoLocation;
import aschworer.astrologer.alexa.service.model.NatalChart;
import aschworer.astrologer.alexa.service.model.Planet;
import aschworer.astrologer.alexa.service.model.Sign;

import java.util.Date;
import java.util.List;

/**
 * @author aschworer
 */
public interface NatalChartsService {

    Sign getPlanetSign(Planet planet, Date date, String lat, String lng) throws Exception;

//    NatalChart getNatalChartByDate(Date date) throws Exception;

    NatalChart getNatalChart(Date date, String lat, String lng) throws Exception;

//    List<GeoLocation> getLocationsByName(String name);

    GeoLocation getCountryByName(String place);
}
