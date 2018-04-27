package aschworer.astrologer.alexa.service;

import aschworer.astrologer.alexa.handler.responder.service.NatalChartsService;
import aschworer.astrologer.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static aschworer.astrologer.alexa.service.AstrologerLambdaService.ASTROLOGER_DATE_FORMAT;

/**
 * @author aschworer
 */
public class LambdaChartsService implements NatalChartsService {
    private static final Logger logger = LoggerFactory.getLogger(LambdaChartsService.class);
//    private GeocodeLocationService locationService = new GeocodeLocationService();
//    private AstrologerLambdaService swissEphLambdaFunction = new AstrologerLambdaService();

    //todo merge these 2 classes - ?
    AstrologerLambdaService service = new AstrologerLambdaService();

    @Override
    public Sign[] getPlanetSign(Planet planet, LocalDate date, String lat, String lng) throws Exception {
        return getNatalChart(date, lat, lng).getSign(planet);
    }

    public NatalChart getNatalChart(LocalDate date, String lat, String lng) throws Exception {
        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");
        Person person = new Person();
        person.setBirthPlace(lat + "," + lng);//todo
        person.setDob(ASTROLOGER_DATE_FORMAT.format(date));
        request.setPerson(person);
        AstrologerLambdaResponse response = service.invoke(request);
        return response.getNatalChart();
    }

}