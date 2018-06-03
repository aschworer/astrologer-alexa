package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.service.locationservice.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import static aschworer.astrologer.alexa.handler.responder.charts.AlexaDateTimeUtil.*;
import static aschworer.astrologer.alexa.handler.responder.charts.AstrologerIntent.*;
import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

/**
 * @author aschworer
 */
public abstract class AstrologerResponder extends Speaker {

    static final String SAY_AS_DATE = "<say-as interpret-as=\"date\">%s</say-as>";
    private static final Logger log = LoggerFactory.getLogger(AstrologerResponder.class);
    private GoogleLocationService locationService = new GoogleLocationService();

    public abstract SpeechletResponse respondToInitialIntent(SessionDetails session);

    public SpeechletResponse handle(Intent intent, SessionDetails session) {
        switch (AstrologerIntent.getByName(intent.getName())) {
            case SUN_SIGN_INTENT:
                if (session.getLastSpokenCard() == null || SpokenCards.WELCOME.equalsIgnoreCase(session.getLastSpokenCard())) {
                    return askForBirthDate();
                } else {
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case FULL_CHART_INTENT:
                if (session.getLastSpokenCard() == null || SpokenCards.WELCOME.equalsIgnoreCase(session.getLastSpokenCard())) {
                    return askForBirthDate();
                } else {
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case PLANET_SIGN_INTENT:
                String planet = intent.getSlot("planet").getValue();
                log.info("planet input: " + planet);
                if (session.getLastSpokenCard() == null || SpokenCards.WELCOME.equalsIgnoreCase(session.getLastSpokenCard())) {
                    session.setPlanet(planet);
                    return askForBirthDate();
                } else {
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case BIRTH_DAY_INTENT:
                String day = intent.getSlot("day").getValue();
                log.info("birth day input: " + day);
                if (session.isAskingForBirthDay()) {
                    session.setBirthDate(day);
                    return respondToBirthDay(session);
                } else {
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case BIRTH_YEAR_OR_TIME_INTENT:
                String slotValue = intent.getSlot("year").getValue();
                log.info("birth year or time input: " + slotValue);
                if (session.isAskingForBirthYear()) {
                    session.setBirthYear(slotValue);
                    session.setBirthDate(replaceYear(slotValue, session.getBirthDate()));
                    return confirmBirthDate(session);
                } else if (session.isAskingForBirthTime()) {
                    session.setBirthTime(slotValue);
                    return respondToInitialIntent(session);
                } else {
                    //repeat last said
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case BIRTH_TIME_INTENT:
                slotValue = intent.getSlot("time").getValue();
                log.info("birth time input: " + slotValue);
                if (session.isAskingForBirthTime()) {
                    session.setBirthTime(slotValue);
                    return respondToInitialIntent(session);
                }
                if (session.isAskingForBirthYear()) {
                    session.setBirthYear(slotValue.replaceAll(":", ""));
                    session.setBirthDate(replaceYear(slotValue, session.getBirthDate()));
                    return confirmBirthDate(session);
                } else {
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case BIRTH_PLACE_INTENT:
                String place = (intent.getSlot("city").getValue() != null) ?
                        intent.getSlot("city").getValue() : intent.getSlot("country").getValue();
                log.info("place input: " + place);
                if ("no".equalsIgnoreCase(place)) {//Alexa bug?
                    return repeat(session.getLastTellMeCard(), session.getLastTellMeSpeech());
                } else if ("yes".equalsIgnoreCase(place)) {//Alexa bug?
                    return handle(Intent.builder().withName(YES_INTENT.getName()).build(), session);
                }
                if (session.isAskingForBirthPlace()) {
                    return lookupAndConfirmBirthPlace(session, place);
                } else {
                    //repeat last said
                    return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
                }
            case I_DONT_KNOW_INTENT:
                if (session.isAskingForBirthYear()) {
                    session.setBirthYear(SessionDetails.UNKNOWN);
                    if (SUN_SIGN_INTENT != session.getInitialIntent()) {
                        return speakAndFinish(MORE_DATA_REQUIRED, "year of birth");
                    }
                } else if (session.isAskingForBirthTime()) {
                    session.setBirthTime(SessionDetails.UNKNOWN);
                } else if (session.isAskingForBirthPlace()) {
                    session.setBirthPlace(SessionDetails.UNKNOWN);
                } else if (session.isAskingForBirthDay()) {
                    return speakAndFinish(MORE_DATA_REQUIRED, "at least a day and a month of birth");
                }
                return respondToInitialIntent(session);
            case YES_INTENT:
                if (session.isAskingForBirthPlace()) {
                    session.setBirthPlaceConfirmed();
                } else if (session.isAskingForBirthDay()) {
                    session.setBirthDateConfirmed();
                }
                return respondToInitialIntent(session);
            case NO_INTENT:
                //repeat last question
                return repeat(session.getLastTellMeCard(), session.getLastTellMeSpeech());
            default:
                //repeat last said
                return repeat(session.getLastSpokenCard(), session.getLastSpokenSpeech());
        }
    }

    /**
     * if the year in the date comes as current year or next year, then 2 options
     * user didnt mention any year. in this case need to ask
     * user actually meant this year or next year. in this case no need to double check, but not sure how to implement this at the moment
     * so fow now leave as is - always double check the year if current
     *
     * @param session
     * @return
     */
    protected SpeechletResponse respondToBirthDay(SessionDetails session) {
        try {
            if (withinAYearFromNow(parseDate(session.getBirthDate()))) {
                return askForBirthYear();
            } else {
                return confirmBirthDate(session);
            }
        } catch (AlexaDateTimeException e) {
            return repeatedSpeech(e.getSpokenCard());
        }
    }

    private SpeechletResponse lookupAndConfirmBirthPlace(SessionDetails session, String place) {
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
        return ask(DOUBLE_CHECK_PLACE, session.getFullBirthPlace());
    }

    /**
     * Rule -
     * <p>
     * If its a SUN sign intent - parse day and month to "20 november" and goto confirm
     * ELSE goto get a year (replace 2015 with new one if present), and then confirm
     */
    SpeechletResponse confirmBirthDate(SessionDetails session) {
        return ask(DOUBLE_CHECK_DATE, String.format(SAY_AS_DATE, session.getBirthDate()));
    }

    private SpeechletResponse askForBirthYear() {
        return ask(WHATS_BIRTH_YEAR);
    }

    SpeechletResponse askForBirthDate() {
        return ask(TELL_ME_BIRTH_DAY);
    }

    SpeechletResponse askForBirthTime() {
        return ask(TELL_ME_BIRTH_TIME);
    }

    SpeechletResponse askForBirthPlace() {
        return ask(TELL_ME_BIRTH_PLACE);
    }

}
