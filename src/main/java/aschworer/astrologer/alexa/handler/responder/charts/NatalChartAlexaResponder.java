package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.service.*;
import aschworer.astrologer.model.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;
import static aschworer.astrologer.alexa.handler.responder.charts.SessionAttributes.*;

/**
 * @author aschworer
 */
public class NatalChartAlexaResponder extends StandardAlexaResponder {

    private static final Logger log = LoggerFactory.getLogger(NatalChartAlexaResponder.class);
    private Astrologer astrologer = new Astrologer();
    private GeocodeLocationService locationService = new GeocodeLocationService();

    @Override
    public SpeechletResponse respondToCustomIntent(Intent intent, Session session) {
        log.info(intent.getName());
        switch (AstrologerIntent.getByName(intent.getName())) {
            case SUN_SIGN_INTENT:
                setInitialIntent(session, AstrologerIntent.SUN_SIGN_INTENT.getName());
                return askForBirthDate();
            case PLANET_SIGN_INTENT:
                setInitialIntent(session, AstrologerIntent.PLANET_SIGN_INTENT.getName());
                session.setAttribute(PLANET, intent.getSlot("planet").getValue());
                return askForBirthDate();
            case FULL_CHART_INTENT:
                setInitialIntent(session, AstrologerIntent.FULL_CHART_INTENT.getName());
                return askForBirthDate();
            case BIRTH_DAY_INTENT:
                return respondToBirthDay(intent, session);
            case BIRTH_YEAR_INTENT:
                final String year = intent.getSlot("year").getValue();
                log.debug(year);
                if (!CURRENT_YEAR.equalsIgnoreCase(year)) session.setAttribute(BIRTH_YEAR, year);
                return doubleCheckDate(session);
            case YES_INTENT:
                //when confirming birthdate
                return respondToBirthDateConfirmation(session);
            case NO_INTENT:
                //when denying birthdate
                return askForBirthDate();
//            case BIRTH_TIME_INTENT:
//                return respondToBirthTime(session, intent.getSlot("time"));
            case BIRTH_PLACE_INTENT:
                return respondToBirthPlace(session, intent.getSlot("place"));
//            case CONFIRM_BIRTH_PLACE_INTENT:
//                return respondToBirthPlaceConfirmation(session);
            default:
                return help();
        }
    }

    private SpeechletResponse respondToBirthDay(Intent intent, Session session) {
        final String day = intent.getSlot("day").getValue();
        log.debug(intent.getSlot("day").getValue());
        session.setAttribute(BIRTH_DATE, day);
        if (!day.startsWith(CURRENT_YEAR) || AstrologerIntent.SUN_SIGN_INTENT.toString().equals(getInitialIntent(session))) {
            return doubleCheckDate(session);
        } else {
            return askForBirthDateYear();
        }
    }

    /**
     * Rule -
     * <p>
     * If its a SUN sign intent - parse day and month to "20 november" and goto confirm
     * ELSE goto get a year (replace 2015 with new one if present), and then confirm
     */
    private SpeechletResponse doubleCheckDate(Session session) {
        String date = (String) session.getAttribute(BIRTH_DATE);
        String year = (String) session.getAttribute(BIRTH_YEAR);
        final String initialIntent = getInitialIntent(session);
        log.info("double check the date - " + date);
        log.info("double check the date. year - " + date);
        log.info("double check the date. initial intent - " + date);
        if (AstrologerIntent.SUN_SIGN_INTENT.toString().equals(initialIntent)) {
            return ask(DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + formatNoYear(date) + "</say-as>");
        } else if (date.startsWith(CURRENT_YEAR)) {
            date = year + date.substring(date.indexOf("-"), date.length());
            session.setAttribute(BIRTH_DATE, date);
        }
        return ask(DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + date + "</say-as>");
    }

    private SpeechletResponse respondToInitialIntent(Session session) {
        final String initial = getInitialIntent(session);
        String date = (String) session.getAttribute(BIRTH_DATE);
        String time = (String) session.getAttribute(BIRTH_TIME); // todo if not null - combine with date
        String place = (String) session.getAttribute(BIRTH_PLACE);
//        String placeFullName = (String) session.getAttribute(BIRTH_PLACE_FULLNAME);
        String lat = (String) session.getAttribute(BIRTH_LAT);
        String lng = (String) session.getAttribute(BIRTH_LNG);
        log.info("initial intent: " + initial);
        log.info("date - " + date);
        if (initial == null) return help();
        switch (AstrologerIntent.getByName(initial)) {
            case SUN_SIGN_INTENT://todo add other planets in sign
                return astrologer.respondToSunSign(date, null, null, null);
            case PLANET_SIGN_INTENT:
//                if (time == null) {
//                    return askForBirthTime();
//                } else
//                if (lat == null || lng == null) {
//                    return askForBirthPlace();
//                }//todo
                return astrologer.respondToPlanetSign(Planet.getByString((String)session.getAttribute("planet")), date, place, lat, lng);
            case FULL_CHART_INTENT:
//                if (lat == null || lng == null) {
//                    return askForBirthPlace();
//                }
                // else if (time == null) {
                // return askForBirthTime();
                // }
                return astrologer.respondWithNatalChart(date, place, lat, lng);
            default:
                return help();
        }
    }

    private SpeechletResponse respondToBirthDateConfirmation(Session session) {
        String clarifiedDate = (String) session.getAttribute(BIRTH_DATE);
        log.info("clarified date - " + clarifiedDate);
        return respondToInitialIntent(session);
    }

    private SpeechletResponse respondToBirthPlace(Session session, Slot slot) {
        final String place = slot.getValue();
        log.debug(slot.getValue());
        session.setAttribute(BIRTH_PLACE, place);
        Map<String, String> coordinates = locationService.getCountryCoordinates(place);
        session.setAttribute(BIRTH_LAT, coordinates.get("lat"));
        session.setAttribute(BIRTH_LNG, coordinates.get("lng"));
//        session.setAttribute(BIRTH_PLACE_FULLNAME, coordinates.get("fullname"));
        return respondToInitialIntent(session);

    }

//    private SpeechletResponse doubleCheckPlace(Session session) {
//        String place = (String) session.getAttribute(BIRTH_PLACE);
//        return ask(DOUBLE_CHECK_DATE, place);
//    }

    /*private SpeechletResponse respondToBirthTime(Session session, Slot slot) {
        log.info("time - " + slot.getValue());
        final String currentIntent = getInitialIntent(session);
        if (currentIntent == null) return help();
        log.info("initial intent: " + currentIntent);
        switch (AstrologerIntent.getByName(currentIntent)) {
            //todo - can be a sun sign too here (sign borders)
            case PLANET_SIGN_INTENT:
                //get a date from session
                return astrologer.respondToMoonSign(slot.getValue());
            case FULL_CHART_INTENT:
                //get a date from session
                return astrologer.respondWithNatalChart(slot.getValue());
            default:
                return help();
        }
    }*/

    private String getInitialIntent(Session session) {
        return (String) session.getAttribute(INITIAL_INTENT);
    }

    private SpeechletResponse askForBirthDateYear() {
        return ask(TELL_ME_BIRTH_YEAR);
    }

    private SpeechletResponse askForBirthDate() {
        return ask(TELL_ME_BIRTH_DAY);
    }

//    private SpeechletResponse askForBirthTime() {
//        return ask(TELL_ME_BIRTH_TIME);
//    }

//    private SpeechletResponse askForBirthPlace() {
//        return ask(TELL_ME_BIRTH_PLACE);
//    }

    private void setInitialIntent(Session session, String name) {
        session.setAttribute(INITIAL_INTENT, name);
    }


}
