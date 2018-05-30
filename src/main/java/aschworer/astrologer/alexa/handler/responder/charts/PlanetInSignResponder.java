package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.time.format.*;

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
//            session.setBirthDateConfirmed();
            String date = session.getBirthDate();
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
            if (withinAYearFromNow(parsedDate)) date = formatNoYear(date);

            String born = String.format(SAY_AS_DATE, date);

            String place = session.getBirthPlace();
            if (place != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(place)) born = born + " in " + place;

            String time = session.getBirthTime();
            LocalTime parsedTime = null;
            if (time != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(time)) {
                parsedTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(ALEXA_TIME_FORMAT).withResolverStyle(ResolverStyle.STRICT));
                born = born + " at " + time;
            }

            Sign[] planetInSign = service.getPlanetSign(session.getPlanet(), parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset());

            if (planetInSign.length > 1) {
                if (time == null) {
                    return askForBirthTime();
                } else if (place == null) {
                    return askForBirthPlace();
                } else {
                    return speakAndFinish(SpokenCards.MORE_DATA_REQUIRED, "time and place of birth");
                }
            } else {
                return speakAndFinish(SpokenCards.SPEAK_PLANET_SIGN, session.getPlanet().toString(), born, planetInSign[0].toString());
            }

        } catch (ParseException e) {
            return repeatedSpeech("InvalidDate");
        } catch (Exception e) {
            log.error("error", e);
            return repeatedSpeech(CHART_ERROR);
        }
    }
}
