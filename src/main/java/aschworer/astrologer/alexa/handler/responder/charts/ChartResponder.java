package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.service.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

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
            //birth date
            if (!session.isBirthDateConfirmed()) {
                return askForBirthDate();
            }
            LocalDate parsedDate = parseDate(session.getBirthDate());
            String born = String.format(SAY_AS_DATE, session.getBirthDate());

            //birth time
            String time = session.getBirthTime();
            LocalTime parsedTime = null;
            if (time == null) {
                return askForBirthTime();
            } else if (!SessionDetails.UNKNOWN.equalsIgnoreCase(time)) {
                parsedTime = parseTime(time);
                born = born + " at " + time;
            }

            //birth place
            String place = session.getBirthPlace();
            if (place != null && session.isBirthPlaceConfirmed()) {
                born = born + " in " + ((session.getFullBirthPlace() != null) ? session.getFullBirthPlace() : place);
            } else if (!SessionDetails.UNKNOWN.equalsIgnoreCase(place)) {
                return askForBirthPlace();
            }

            //give result
            return speakAndFinish(SpokenCards.SPEAK_NATAL_CHART, born,
                    getNatalChartAsSpeech(
                            service.getNatalChart(parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset()),
                            getMissingInfoPhrase(session.getFullBirthPlace(), time)
                    ));
        } catch (AlexaDateException e) {
            log.error("Date parse problem", e);
            return repeatedSpeech(INVALID_DATE);
        } catch (AlexaTimeException e) {
            log.error("Time parse problem", e);
            return repeatedSpeech(INVALID_TIME);
        } catch (Exception e) {
            log.error("error", e);
            return speakAndFinish(CHART_ERROR);
        }
    }
}
