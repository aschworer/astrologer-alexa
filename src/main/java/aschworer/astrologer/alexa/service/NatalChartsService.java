package aschworer.astrologer.alexa.service;

import aschworer.astrologer.model.NatalChart;
import aschworer.astrologer.model.Planet;
import aschworer.astrologer.model.Sign;

import java.time.*;

/**
 * @author aschworer
 */
public interface NatalChartsService {

    Sign[] getPlanetSign(Planet planet, LocalDate date, LocalTime time, String lat, String lng, String timeZoneOffset) throws Exception;

    NatalChart getNatalChart(LocalDate date, LocalTime time, String lat, String lng, String timeZoneOffset) throws Exception;

}
