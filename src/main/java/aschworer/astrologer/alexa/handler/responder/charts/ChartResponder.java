package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.service.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.AlexaDateTimeUtil.*;
import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

@NoArgsConstructor
public class ChartResponder extends AstrologerResponder {

    private static final Logger log = LoggerFactory.getLogger(ChartResponder.class);
    protected NatalChartsService service;

    public ChartResponder(SessionDetails sessionDetails) {
        sessionDetails.setInitialIntent(AstrologerIntent.FULL_CHART_INTENT);
        ResourceBundle config = ResourceBundle.getBundle("application");
        if (Boolean.valueOf(config.getString("mock"))) {
            service = new MockAlexaNatalChartsService();
        } else {
            service = new ChartsService();
        }
    }

    @Override
    public SpeechletResponse respondToInitialIntent(SessionDetails session) {
        try {
            LocalDate parsedDate = parseDate(session.getBirthDate());
            String born = String.format(SAY_AS_DATE, session.getBirthDate());

            String place = session.getBirthPlace();
            if (place == null) {
                return askForBirthPlace();
            } else if (!SessionDetails.UNKNOWN.equalsIgnoreCase(place)) born = born + " in " + place;

            String time = session.getBirthTime();
            LocalTime parsedTime = null;
            if (time == null) {
                return askForBirthTime();
            } else if (!SessionDetails.UNKNOWN.equalsIgnoreCase(time)) {
                parsedTime = parseTime(time);
                born = born + " at " + time;
            }
            return speakAndFinish(SpokenCards.SPEAK_NATAL_CHART, born,
                    getNatalChartAsString(service.getNatalChart(parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset())));
        } catch (ParseException e) {
            return repeatedSpeech("InvalidDate");//todo
        } catch (Exception e) {
            log.error("error", e);
            return speakAndFinish(CHART_ERROR);
        }
    }

}
