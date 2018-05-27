package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.service.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

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
            String date = session.getBirthDate();
            if (date == null) {
                return askForBirthTime();
            }
            session.setBirthDateConfirmed();

            String time = session.getBirthTime();
            LocalTime parsedTime;
            if (time == null) {
                return askForBirthTime();
            } else {
                parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(ALEXA_TIME_FORMAT).withResolverStyle(ResolverStyle.STRICT));
            }

            String place = session.getBirthPlace();
            if (place == null) {
                return askForBirthPlace();
            }

            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));

            return speakAndFinish(SpokenCards.SPEAK_NATAL_CHART, String.format(SAY_AS_DATE, date) + " born in " + place + " at " + time,
                    getNatalChartAsString(service.getNatalChart(parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset())));
        } catch (ParseException e) {
            return repeatedSpeech("InvalidDate");//todo
        } catch (Exception e) {
            log.error("error", e);
            return repeatedSpeech("NatalChartError");//todo
        }
    }

}
