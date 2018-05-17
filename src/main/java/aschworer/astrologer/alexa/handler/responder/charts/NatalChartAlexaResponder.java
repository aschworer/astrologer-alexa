package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.service.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import java.text.*;
import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

/**
 * @author aschworer
 */
public class NatalChartAlexaResponder extends StandardAlexaResponder {

    public static final String CURRENT_YEAR = new SimpleDateFormat("yyyy").format(new Date());

    private static final Logger log = LoggerFactory.getLogger(NatalChartAlexaResponder.class);
    private Astrologer astrologer = new Astrologer();
    private GeocodeLocationService locationService = new GeocodeLocationService();

    @Override
    public SpeechletResponse respondToCustomIntent(Intent intent, Session session) {
        log.info(intent.getName());
        SessionDetails sessionDetails = new SessionDetails(session);
        switch (AstrologerIntent.getByName(intent.getName())) {
            case SUN_SIGN_INTENT:
                sessionDetails.setInitialIntent(AstrologerIntent.SUN_SIGN_INTENT);
                return askForBirthDate();
            case PLANET_SIGN_INTENT:
                sessionDetails.setInitialIntent(AstrologerIntent.PLANET_SIGN_INTENT);
                sessionDetails.setPlanet(intent.getSlot("planet").getValue());
                return askForBirthDate();
            case FULL_CHART_INTENT:
                sessionDetails.setInitialIntent(AstrologerIntent.FULL_CHART_INTENT);
                return askForBirthDate();
            case BIRTH_DAY_INTENT:
                return respondToBirthDay(intent, sessionDetails);
            case BIRTH_YEAR_INTENT:
                final String year = intent.getSlot("year").getValue();
                log.debug(year);
                if (!CURRENT_YEAR.equalsIgnoreCase(year)) sessionDetails.setYear(year);
                return doubleCheckDate(sessionDetails);
            case YES_INTENT:
                //when confirming birthdate
                return respondToBirthDateConfirmation(sessionDetails);
            case NO_INTENT:
                //when denying birthdate
                return askForBirthDate();
//            case BIRTH_TIME_INTENT:
//                return respondToBirthTime(session, intent.getSlot("time"));
            case BIRTH_PLACE_INTENT:
                return respondToBirthPlace(sessionDetails, intent.getSlot("place"));
//            case CONFIRM_BIRTH_PLACE_INTENT:
//                return respondToBirthPlaceConfirmation(session);
            default:
                return help();
        }
    }

    private SpeechletResponse respondToBirthDay(Intent intent, SessionDetails session) {
        final String day = intent.getSlot("day").getValue();
        log.debug(day);
        session.setBirthDate(day);
        if (!day.startsWith(CURRENT_YEAR) || session.isSunSignRequested()) {
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
    private SpeechletResponse doubleCheckDate(SessionDetails session) {
        String date = session.getBirthDate();
        String year = session.getBirthYear();
        log.info("double check the birthDate - " + date);
        log.info("double check the birthDate. year - " + date);
        log.info("double check the birthDate. initial intent - " + date);
        if (session.isSunSignRequested()) {
            return ask(DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + formatNoYear(date) + "</say-as>");
        } else if (date.startsWith(CURRENT_YEAR)) {
            date = year + date.substring(date.indexOf("-"), date.length());
            session.setBirthDate(date);
        }
        return ask(DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + date + "</say-as>");
    }

    private SpeechletResponse respondToInitialIntent(SessionDetails session) {
        String date = session.getBirthDate();
//        String time = (String) session.getAttribute(BIRTH_TIME); // todo if not null - combine with birthDate
        String place = session.getBirthPlace();
//        String placeFullName = (String) session.getAttribute(BIRTH_PLACE_FULLNAME);
        String lat = session.getBirthLat();;
        String lng = session.getBirthLng();;
//        log.info("initial intent: " + initial);
        log.info("birthDate - " + date);
        if (!session.isInitialIntentPresent()) return help();
        switch (session.getInitialIntent()) {
            case SUN_SIGN_INTENT://todo add other planets in sign
                return astrologer.respondToSunSign(date, null, null, null);
            case PLANET_SIGN_INTENT:
//                if (time == null) {
//                    return askForBirthTime();
//                } else
//                if (lat == null || lng == null) {
//                    return askForBirthPlace();
//                }//todo
                return astrologer.respondToPlanetSign(session.getPlanet(), date, place, lat, lng);
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

    private SpeechletResponse respondToBirthDateConfirmation(SessionDetails session) {
        log.info("clarified birthDate - " + session.getBirthDate());
        return respondToInitialIntent(session);
    }

    private SpeechletResponse respondToBirthPlace(SessionDetails session, Slot slot) {
        final String place = slot.getValue();
        log.debug(slot.getValue());
        session.setBirthPlace(place);
        Map<String, String> coordinates = locationService.getCountryCoordinates(place);
        session.setBirthLat(coordinates.get("lat"));
        session.setBirthLng(coordinates.get("lng"));
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
                //get a birthDate from session
                return astrologer.respondToMoonSign(slot.getValue());
            case FULL_CHART_INTENT:
                //get a birthDate from session
                return astrologer.respondWithNatalChart(slot.getValue());
            default:
                return help();
        }
    }*/

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

}
