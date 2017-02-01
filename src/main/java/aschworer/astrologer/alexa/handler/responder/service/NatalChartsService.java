package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.service.model.NatalChart;
import aschworer.astrologer.alexa.service.model.Planet;
import aschworer.astrologer.alexa.service.model.Sign;

import java.util.Date;

/**
 * @author aschworer
 */
public interface NatalChartsService {

    Sign getPlanetSign(Planet planet, Date date) throws Exception;

    NatalChart getNatalChartByDate(Date date) throws Exception;

    NatalChart getNatalChartByDateAndPlace(Date date, String place) throws Exception;

}
