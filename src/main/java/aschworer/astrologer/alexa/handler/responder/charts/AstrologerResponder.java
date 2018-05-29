package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.service.locationservice.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import java.time.*;
import java.time.format.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

/**
 * @author aschworer
 */
public abstract class AstrologerResponder extends Speaker {

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
                    session.setBirthDate(adjustDateWithYear(slotValue, session.getBirthDate()));
                    session.setBirthYear(slotValue);
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

    private String adjustDateWithYear(String slotValue, String date) {
        if (slotValue != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(slotValue)) {
            return slotValue + date.substring(date.indexOf("-"), date.length());
        }
        return date;
    }

    protected SpeechletResponse respondToBirthDay(SessionDetails session) {
        LocalDate parsedDate = LocalDate.parse(session.getBirthDate(), DateTimeFormatter.ofPattern(ALEXA_DATE_FORMAT));
        //! - if the year in the date comes as current year or next year, then 2 options
        // user didnt mention any year. in this case need to ask
        // user actually meant this year or next year. in this case no need to double check, but not sure how to implement this at the moment
        // so fow now leave as is - always double check the year if current
        if (withinAYearFromNow(parsedDate)) {
            return askForBirthYear();
        } else {
            return doubleCheckDate(session);
        }
    }

    protected boolean withinAYearFromNow(LocalDate date) {
        LocalDate withinAYear = LocalDate.of(LocalDate.now().getYear() + 1, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        return (date.isBefore(withinAYear) && date.isAfter(LocalDate.now())) || date.isEqual(LocalDate.now());
    }

    public static void main(String[] args) {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now();
        System.out.println(date1.isEqual(date2));
    }

    /**
     * Rule -
     * <p>
     * If its a SUN sign intent - parse day and month to "20 november" and goto confirm
     * ELSE goto get a year (replace 2015 with new one if present), and then confirm
     */
    private SpeechletResponse doubleCheckDate(SessionDetails session) {
        return ask(DOUBLE_CHECK_DATE, String.format(SAY_AS_DATE, session.getBirthDate()));
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


    private SpeechletResponse askForBirthYear() {
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
