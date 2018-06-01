package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.time.*;
import java.time.format.*;

import static aschworer.astrologer.alexa.handler.responder.charts.AlexaDateTimeUtil.*;
import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

@NoArgsConstructor
public class PlanetInSignResponder extends ChartResponder {

    private static final Logger log = LoggerFactory.getLogger(PlanetInSignResponder.class);

    public PlanetInSignResponder(SessionDetails sessionDetails) {
        super(sessionDetails);
        sessionDetails.setInitialIntent(AstrologerIntent.PLANET_SIGN_INTENT);
    }

    @Override
    public SpeechletResponse respondToInitialIntent(SessionDetails session) {
        try {
            //birth date
            if (!session.isBirthDateConfirmed()) {
                return askForBirthDate();
            }
            String date = session.getBirthDate();
            LocalDate parsedDate = parseDate(session.getBirthDate());
            if (withinAYearFromNow(parsedDate)) date = formatNoYear(date);
            String born = String.format(SAY_AS_DATE, date);

            //birth time
            String time = session.getBirthTime();
            LocalTime parsedTime = null;
            if (time != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(time)) {
                parsedTime = parseTime(time);
                born = born + " at " + parsedTime.format(DateTimeFormatter.ofPattern("hh.mm a"));
            }

            //birth place
            String place = session.getBirthPlace();
            if (place != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(place)) {
                if (session.isBirthPlaceConfirmed()) {
                    born = born + " in " + ((session.getFullBirthPlace() != null) ? session.getFullBirthPlace() : place);
                } else {
                    return askForBirthPlace();
                }
            }

            //result
            Sign[] planetInSign = service.getPlanetSign(session.getPlanet(), parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset());

            if (planetInSign.length > 1) {
                if (time == null) {
                    return askForBirthTime();
                } else if (place == null) {
                    return askForBirthPlace();
                } else {
                    return speakAndFinish(SpokenCards.MORE_DATA_REQUIRED, getMissingInfoPhrase(place, time));
                }
            } else {
                return speakAndFinish(SpokenCards.SPEAK_PLANET_SIGN, session.getPlanet().toString(), born, planetInSign[0].toString());
            }
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
