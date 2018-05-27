package aschworer.astrologer.alexa.service;

import aschworer.astrologer.alexa.service.flatlib.*;
import aschworer.astrologer.model.*;
import org.slf4j.*;

import java.time.*;
import java.util.*;

import static aschworer.astrologer.alexa.service.flatlib.FlatLibBirthDetails.*;

/**
 * @author aschworer
 */
public class ChartsService implements NatalChartsService {
    private static final Logger logger = LoggerFactory.getLogger(ChartsService.class);
    //    private GeocodeLocationService locationService = new GeocodeLocationService();
    //todo merge these 2 classes - ?
    private FlatLibService service = new FlatLibService();

    @Override
    public Sign[] getPlanetSign(Planet planet, LocalDate date, LocalTime time, String lat, String lng, String timeZoneOffset) throws Exception {
        return getNatalChart(date, time, lat, lng, timeZoneOffset).getSign(planet);
    }

    public NatalChart getNatalChart(LocalDate date, LocalTime time, String lat, String lng, String timeZoneOffset) throws Exception {
//        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");

        NatalChart natalChart = new NatalChart();
        Person person = new Person();
        person.setBirthPlace(lat + "," + lng);//todo
        person.setDob(date);
        natalChart.setPerson(person);

        FlatLibBirthDetails flatLibBirthDetails = new FlatLibBirthDetails();
        flatLibBirthDetails.setDate(FLATLIB_DATE_FORMATTER.format(date));

        if (time != null) flatLibBirthDetails.setTime(FLATLIB_TIME_FORMATTER.format(time));
        if (timeZoneOffset != null) flatLibBirthDetails.setTimezone(timeZoneOffset);
        if (lat != null) flatLibBirthDetails.setLat(lat);
        if (lng != null) flatLibBirthDetails.setLng(lng);

        List<CharacteristicInSign> response = service.invoke(flatLibBirthDetails);
        natalChart.setCharacteristicsInSigns(response);
        return natalChart;
    }

}