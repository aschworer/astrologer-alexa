package aschworer.astrologer.alexa.service;

import aschworer.astrologer.alexa.handler.responder.service.NatalChartsService;
import aschworer.astrologer.alexa.service.flatlib.FlatLibBirthDetails;
import aschworer.astrologer.alexa.service.flatlib.FlatLibService;
import aschworer.astrologer.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static aschworer.astrologer.alexa.handler.responder.service.Astrologer.ALEXA_DATE_FORMAT;
import static aschworer.astrologer.alexa.service.flatlib.FlatLibService.FLATLIB_DATE_FORMATTER;

/**
 * @author aschworer
 */
public class ChartsService implements NatalChartsService {
    private static final Logger logger = LoggerFactory.getLogger(ChartsService.class);
//    private GeocodeLocationService locationService = new GeocodeLocationService();
    //todo merge these 2 classes - ?
    private FlatLibService service = new FlatLibService();

    @Override
    public Sign[] getPlanetSign(Planet planet, LocalDate date, String lat, String lng) throws Exception {
        return getNatalChart(date, lat, lng).getSign(planet);
    }

    public NatalChart getNatalChart(LocalDate date, String lat, String lng) throws Exception {
//        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");
        NatalChart natalChart = new NatalChart();
        Person person = new Person();
        person.setBirthPlace(lat + "," + lng);//todo
        person.setDob(DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT).format(date));
        natalChart.setPerson(person);

        FlatLibBirthDetails flatLibBirthDetails = new FlatLibBirthDetails();
        flatLibBirthDetails.setDate(FLATLIB_DATE_FORMATTER.format(date));
//        if (time != null) {
//            flatLibBirthDetails.setTime(time);
//            if (birthLocation != null) flatLibBirthDetails.setTimezone(birthLocation.getTimezoneOffset());
//        }
//        if (birthLocation != null && birthLocation.getLng() != null) {
            flatLibBirthDetails.setLng(lng);
//        }
//        if (birthLocation != null && birthLocation.getLat() != null) {
            flatLibBirthDetails.setLat(lat);
//        }

        List<CharacteristicInSign> response = service.invoke(flatLibBirthDetails);
        natalChart.setCharacteristicsInSigns(response);
        return natalChart;
    }

}