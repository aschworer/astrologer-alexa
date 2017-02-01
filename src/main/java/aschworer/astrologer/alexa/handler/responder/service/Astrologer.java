package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.handler.responder.Speaker;
import aschworer.astrologer.alexa.service.MockAlexaNatalChartsService;
import aschworer.astrologer.alexa.service.NatalChartsServiceImpl;
import aschworer.astrologer.alexa.service.model.Planet;
import com.amazon.speech.speechlet.SpeechletResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public class Astrologer extends Speaker {

    public static final SimpleDateFormat ALEXA_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private NatalChartsService service;

    public Astrologer() {
        ResourceBundle config = ResourceBundle.getBundle("application");
        if (Boolean.valueOf(config.getString("mock"))) {
            service = new MockAlexaNatalChartsService();
        } else {
            service = new NatalChartsServiceImpl();
        }
    }

    SpeechletResponse respondToSunSign(String dateSlotValue) {
        return respondToPlanetSign(Planet.SUN, dateSlotValue);
    }

    SpeechletResponse respondToMoonSign(String dateSlotValue) {
        return respondToPlanetSign(Planet.MOON, dateSlotValue);
    }

    SpeechletResponse respondToNatalChart(String dateSlotValue) {
        try {
            Date date = ALEXA_DATE_FORMAT.parse(dateSlotValue);
            return speak(Cards.SPEAK_NATAL_CHART, "<say-as interpret-as=\"date\">" + dateSlotValue + "</say-as>", getNatalChartAsString(service.getNatalChartByDate(date)));
        } catch (ParseException e) {
            return ask("InvalidDate");
        } catch (Exception e) {
            return ask("NatalChartError");
        }

    }

    private SpeechletResponse respondToPlanetSign(Planet planet, String dateSlotValue) {
        try {
            Date date = ALEXA_DATE_FORMAT.parse(dateSlotValue);
            return speak(Cards.SPEAK_PLANET_SIGN, planet.toString(), "<say-as interpret-as=\"date\">" + dateSlotValue + "</say-as>", service.getPlanetSign(planet, date).toString());
        } catch (ParseException e) {
            return ask("InvalidDate");
        } catch (Exception e) {
            return ask("NatalChartError");
        }
    }

}
