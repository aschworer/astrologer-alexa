package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;
import lombok.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.time.format.*;

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
            String date = session.getBirthDate();
            String place = session.getBirthPlace();
            LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
            session.setBirthDateConfirmed();
            LocalTime parsedTime = null;
            if (session.getBirthTime() != null) {
                parsedTime = LocalTime.parse(session.getBirthTime(), DateTimeFormatter.ofPattern(ALEXA_TIME_FORMAT).withResolverStyle(ResolverStyle.STRICT));
            }
            Sign[] planetInSign = service.getPlanetSign(session.getPlanet(), parsedDate, parsedTime, session.getBirthLat(), session.getBirthLng(), session.getBirthTimeZoneOffset());

            if (planetInSign.length > 1 && parsedTime == null) {
                //check if birth time present
                return askForBirthTime();
            } else if (planetInSign.length > 1 && place == null) {
                return askForBirthPlace();
            } else {
                //todo setBirthTimeConfirmed, for chart too
                if (withinAYearFromNow(parsedDate)) {
                    date = formatNoYear(date);
                }
                String placeAndTimeOfBirth = (place != null) ? " in " + place : "";
                placeAndTimeOfBirth = placeAndTimeOfBirth + ((session.getBirthTime() != null) ? " at " + session.getBirthTime() : "");
                return speakAndFinish(SpokenCards.SPEAK_PLANET_SIGN, session.getPlanet().toString(), String.format(SAY_AS_DATE, date) +
                        placeAndTimeOfBirth, planetInSign[0].toString());//todo if at this point still more than one - error
            }
        } catch (ParseException e) {
            return repeatedSpeech("InvalidDate");
        } catch (Exception e) {
            log.error("error", e);
            return repeatedSpeech("NatalChartError");
        }
    }
}
