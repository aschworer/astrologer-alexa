package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.handler.responder.Speaker;
import aschworer.astrologer.alexa.service.LambdaChartsService;
import aschworer.astrologer.alexa.service.MockAlexaNatalChartsService;
import aschworer.astrologer.model.Planet;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public class Astrologer extends Speaker {

    private static final SimpleDateFormat ALEXA_DATE_FORMAT_SIMPLE_DF = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ALEXA_DATE_FORMAT = "yyyy-MM-dd";

    private NatalChartsService service;

    public Astrologer() {
        ResourceBundle config = ResourceBundle.getBundle("application");
        if (Boolean.valueOf(config.getString("mock"))) {
            service = new MockAlexaNatalChartsService();
        } else {
            service = new LambdaChartsService();
        }
    }

    SpeechletResponse respondToSunSign(String date, String placeFullName, String lat, String lng) {
        try {
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
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
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
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
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
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
