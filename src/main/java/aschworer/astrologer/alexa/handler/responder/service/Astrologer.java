package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.handler.responder.Speaker;
import aschworer.astrologer.alexa.service.MockAlexaNatalChartsService;
import aschworer.astrologer.alexa.service.NatalChartsServiceImpl;
import aschworer.astrologer.alexa.service.model.GeoLocation;
import aschworer.astrologer.alexa.service.model.Planet;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public class Astrologer extends Speaker {

    private static final SimpleDateFormat ALEXA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private NatalChartsService service;

    public Astrologer() {
        ResourceBundle config = ResourceBundle.getBundle("application");
        if (Boolean.valueOf(config.getString("mock"))) {
            service = new MockAlexaNatalChartsService();
        } else {
            service = new NatalChartsServiceImpl();
        }
    }

    SpeechletResponse respondToSunSign(String date, String placeFullName, String lat, String lng) {
        try {
            Date parsedDate = ALEXA_DATE_FORMAT.parse(date);
            String placeOfBirth = "";
            if (lat != null && lng != null) {
                placeOfBirth = placeOfBirth + " born in " + placeFullName;
            }
            return speak(Cards.SPEAK_PLANET_SIGN, Planet.SUN.getString(), "<say-as interpret-as=\"date\">" + formatNoYear(date) + "</say-as>" + placeOfBirth,
                    service.getPlanetSign(Planet.SUN, parsedDate, lat, lng).toString());
        } catch (ParseException e) {
            return ask("InvalidDate");
        } catch (Exception e) {
            return ask("NatalChartError");
        }
    }

    SpeechletResponse respondToMoonSign(String date, String placeFullName, String lat, String lng) {
        return respondToPlanetSign(Planet.MOON, date, placeFullName, lat, lng);
    }

    SpeechletResponse respondWithNatalChart(String date, String placeFullName, String lat, String lng) {
        try {
            Date parsedDate = ALEXA_DATE_FORMAT.parse(date);
            String placeOfBirth = "";
            if (lat != null && lng != null) {
                placeOfBirth = placeOfBirth + " in " + placeFullName;
            }
            return speak(Cards.SPEAK_NATAL_CHART, "<say-as interpret-as=\"date\">" + date + "</say-as>" + placeOfBirth, getNatalChartAsString(service.getNatalChart(parsedDate, lat, lng)));
        } catch (ParseException e) {
            return ask("InvalidDate");
        } catch (Exception e) {
            return ask("NatalChartError");
        }
    }

    private SpeechletResponse respondToPlanetSign(Planet planet, String date, String placeFullName, String lat, String lng) {
        try {
            Date parsedDate = ALEXA_DATE_FORMAT.parse(date);
            String placeOfBirth = "";
            if (lat != null && lng != null) {
                placeOfBirth = placeOfBirth + " born in " + placeFullName;
            }
            return speak(Cards.SPEAK_PLANET_SIGN, planet.toString(), "<say-as interpret-as=\"date\">" + date + "</say-as>" + placeOfBirth, service.getPlanetSign(planet, parsedDate, lat, lng).toString());
        } catch (ParseException e) {
            return ask("InvalidDate");
        } catch (Exception e) {
            return ask("NatalChartError");
        }
    }

    public Map<String, String> getCountryCoordinates(String place) {
        HashMap<String, String> coordinates = new HashMap<>();
        try {
            final GeoLocation countryLocation = service.getCountryByName(place);
            coordinates.put("lat", countryLocation.getLat());
            coordinates.put("lng", countryLocation.getLng());
            coordinates.put("fullname", countryLocation.getFullName());
            return coordinates;
        } catch (Exception e) {
            //todo
            return null;
        }
    }

//    SpeechletResponse getLocationsByName(String name) {
//        try {
//            StringBuilder placesList = new StringBuilder();
//            placesList.append("")
//            final List<GeoLocation> locationsByName = service.getLocationsByName(name);
//            for (GeoLocation location : locationsByName) {
//                placesList.append(location.getFullName()).append(", or ");
//            }
//            return ask(Cards.SELECT_PLACE, placesList.toString());
//        } catch (Exception e) {
//            return ask("NatalChartError");
//        }
//    }
}
