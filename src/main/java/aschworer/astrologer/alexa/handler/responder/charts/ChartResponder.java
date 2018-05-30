package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.service.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.CHART_ERROR;

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
            LocalDate parsedDate = LocalDate.parse(session.getBirthDate(), DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
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
                parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(ALEXA_TIME_FORMAT).withResolverStyle(ResolverStyle.STRICT));
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
