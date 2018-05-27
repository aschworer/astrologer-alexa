package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.service.locationservice.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import java.text.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

/**
 * @author aschworer
 */
public abstract class AstrologerResponder extends Speaker {

    public static final String CURRENT_YEAR = new SimpleDateFormat("yyyy").format(new Date());
    static final String SAY_AS_DATE = "<say-as interpret-as=\"date\">%s</say-as>";
    static final String ALEXA_DATE_FORMAT = "yyyy-MM-dd";
    static final String ALEXA_TIME_FORMAT = "HH:mm";
    private static final Logger log = LoggerFactory.getLogger(AstrologerResponder.class);
    private GoogleLocationService locationService = new GoogleLocationService();

    public abstract SpeechletResponse respondToInitialIntent(SessionDetails session);

    public SpeechletResponse handle(Intent intent, SessionDetails session) {
        log.info(intent.getName());
        log.info(session.toString());
        //return respondToInitialIntent(intent, session) todo remove all that from here and move to custom responders
        switch (AstrologerIntent.getByName(intent.getName())) {
            case SUN_SIGN_INTENT:
                return askForBirthDate();
            case FULL_CHART_INTENT:
                return askForBirthDate();
            case PLANET_SIGN_INTENT:
                session.setPlanet(intent.getSlot("planet").getValue());
                return askForBirthDate();
            case BIRTH_DAY_INTENT:
                session.setBirthDate(intent.getSlot("day").getValue());
                return respondToBirthDay(session);
            case BIRTH_YEAR_OR_TIME_INTENT:
                final String slotValue = intent.getSlot("year").getValue();
                if (session.isAskingForBirthYear()) {
                    if (!CURRENT_YEAR.equalsIgnoreCase(slotValue)) session.setYear(slotValue);//todo add next year too
                    return doubleCheckDate(session);
                } else if (session.isAskingForBirthTime()) {
                    //                if (session.isBirthDateConfirmed()) {//must be birth time
                    LocalTime parsedTime = LocalTime.parse(slotValue, DateTimeFormatter.ofPattern("HHmm"));
                    session.setBirthTime(DateTimeFormatter.ofPattern(ALEXA_TIME_FORMAT).format(parsedTime));
                    return respondToInitialIntent(session);
                } else {
                    //repeat last said
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case BIRTH_TIME_INTENT:
                session.setBirthTime(intent.getSlot("time").getValue());
                return respondToInitialIntent(session);
            case BIRTH_PLACE_INTENT:
                //todo check if it's the right time to respond to birthplace
                //todo if previous card was asking for place then good. if not - repeat card
                if (session.isAskingForBirthPlace()) {
                    return respondToBirthPlace(session, intent.getSlot("place").getValue());
                } else {
                    //repeat last said
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case YES_INTENT:
                return respondToInitialIntent(session);
            case NO_INTENT:
                //repeat last question
                return repeat(session.getLastTellMeCard(), session.getLastTellMeSpeech());
//                return respondToNo(session);
            default:
                //repeat last said
                return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
        }
    }

    protected SpeechletResponse respondToBirthDay(SessionDetails session) {
        String birthDate = session.getBirthDate();
        LocalDate parsedDate = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
        if (birthDate.startsWith(CURRENT_YEAR) || parsedDate.isAfter(LocalDate.now())) {
            return askForBirthDateYear();
        } else {
            return doubleCheckDate(session);
        }
    }

    /**
     * Rule -
     * <p>
     * If its a SUN sign intent - parse day and month to "20 november" and goto confirm
     * ELSE goto get a year (replace 2015 with new one if present), and then confirm
     */
    private SpeechletResponse doubleCheckDate(SessionDetails session) {
        String date = session.getBirthDate();
        String year = session.getBirthYear();
        log.info("double check the birthDate - " + date);
        log.info("double check the birthDate. year - " + year);
        if (date.startsWith(CURRENT_YEAR)) {
            date = year + date.substring(date.indexOf("-"), date.length());
            session.setBirthDate(date);
        }
        return ask(DOUBLE_CHECK_DATE, String.format(SAY_AS_DATE, date));
    }

    private SpeechletResponse respondToBirthPlace(SessionDetails session, String place) {
        session.setBirthPlace(place);
        try {
            Location location = locationService.getFirstLocationByName(place);
            session.setBirthLat(location.getLat());
            session.setBirthLng(location.getLng());
            session.setBirthTimeZoneOffset(location.getTimezoneOffset());
            session.setBirthPlaceFull(location.getFullName());
        } catch (Exception e) {
            log.error("Location retrieval problem: ", e);
            return askForBirthPlace();
        }
        return doubleCheckPlace(session);
    }

    private SpeechletResponse doubleCheckPlace(SessionDetails session) {
        return ask(DOUBLE_CHECK_PLACE, session.getFullBirthPlace());
    }


    private SpeechletResponse askForBirthDateYear() {
        return ask(TELL_ME_BIRTH_YEAR);
    }

    private SpeechletResponse askForBirthDate() {
        return ask(TELL_ME_BIRTH_DAY);
    }

    SpeechletResponse askForBirthTime() {
        return ask(TELL_ME_BIRTH_TIME);
    }

    SpeechletResponse askForBirthPlace() {
        return ask(TELL_ME_BIRTH_PLACE);
    }

}
