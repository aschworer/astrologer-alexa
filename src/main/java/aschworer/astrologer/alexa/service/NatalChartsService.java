package aschworer.astrologer.alexa.service;

import aschworer.astrologer.model.NatalChart;
import aschworer.astrologer.model.Planet;
import aschworer.astrologer.model.Sign;

import java.time.LocalDate;

/**
 * @author aschworer
 */
public interface NatalChartsService {

    Sign[] getPlanetSign(Planet planet, LocalDate date, String lat, String lng) throws Exception;

//    NatalChart getNatalChartByDate(Date date) throws Exception;

    NatalChart getNatalChart(LocalDate date, String lat, String lng) throws Exception;

//    List<GeoLocation> getLocationsByName(String name);

//    GeoLocation getCountryByName(String place);
}
