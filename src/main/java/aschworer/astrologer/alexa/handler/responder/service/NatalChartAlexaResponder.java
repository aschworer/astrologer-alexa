package aschworer.astrologer.alexa.handler.responder.service;

import aschworer.astrologer.alexa.handler.responder.StandardAlexaResponder;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author aschworer
 */
public class NatalChartAlexaResponder extends StandardAlexaResponder {

    public static final String INITIAL_INTENT = "initial_intent";
    public static final String DATE = "date";
    public static final String YEAR = "year";
    private static final Logger log = LoggerFactory.getLogger(NatalChartAlexaResponder.class);
    private static final String CURRENT_YEAR = new SimpleDateFormat("yyyy").format(new Date());

    private Astrologer astrologer = new Astrologer();

    @Override
    public SpeechletResponse respondToCustomIntent(Intent intent, Session session) throws SpeechletException {
        log.info(intent.getName());
        switch (NatalChartIntent.getByName(intent.getName())) {
            case SUN_SIGN_INTENT:
                setInitialIntent(session, NatalChartIntent.SUN_SIGN_INTENT.getName());
                return askForBirthDate();
            case MOON_SIGN_INTENT:
                setInitialIntent(session, NatalChartIntent.MOON_SIGN_INTENT.getName());
                return askForBirthDate();
            case NATAL_CHART_INTENT:
                setInitialIntent(session, NatalChartIntent.NATAL_CHART_INTENT.getName());
                return askForBirthDate();
            case BIRTH_DAY_INTENT:
                final String day = intent.getSlot("day").getValue();
                log.debug(intent.getSlot("day").getValue());
                session.setAttribute(DATE, day);
                if (!day.startsWith(CURRENT_YEAR) || NatalChartIntent.SUN_SIGN_INTENT.toString().equals(getInitialIntent(session))) {
                    return doubleCheckDate(session);
                } else {
                    return askForBirthDateYear();
                }
            case CONFIRM_DATE_INTENT:
                return respondToBirthDate(session);
            case DENY_DATE_INTENT:
                return askForBirthDate();
            case BIRTH_YEAR_INTENT:
                log.debug(intent.getSlot("year").getValue());
                session.setAttribute(YEAR, intent.getSlot("year").getValue());
                return doubleCheckDate(session);
            case BIRTH_TIME_INTENT:
                return respondToBirthTime(session, intent.getSlot("time"));
            case BIRTH_PLACE_INTENT:
                return respondToBirthPlace(session, intent.getSlot("place"));
            case NA:
                throw new SpeechletException("Invalid NatalChartIntent");
            default:
                return help();
        }
    }

//    public static void main(String[] args) {
//        System.out.println(new SimpleDateFormat("yyyy").format(new Date()));
//    }

    /**
     * Rule -
     * <p>
     * If its a SUN sign intent - parse day and month to "20 november" and goto confirm
     * ELSE goto get a year (replace 2015 with new one if present), and then confirm
     */
    private SpeechletResponse doubleCheckDate(Session session) {
        String date = (String) session.getAttribute(DATE);
        String year = (String) session.getAttribute(YEAR);

        if (NatalChartIntent.SUN_SIGN_INTENT.toString().equals(getInitialIntent(session))) {
            return ask(Cards.DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + format(date) + "</say-as>");
        } else if (date.startsWith(CURRENT_YEAR)) {
            date = year + date.substring(date.indexOf("-"), date.length());
            session.setAttribute(DATE, date);
        }
        return ask(Cards.DOUBLE_CHECK_DATE, "<say-as interpret-as=\"date\">" + date + "</say-as>");
    }

    private String format(String date) {
        try {
            return new SimpleDateFormat("dd MMMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (Exception e) {
            log.error("Date re-format parse exception: " + date);
            return null;
        }
    }

    private SpeechletResponse respondToBirthDate(Session session) {
        String clarifiedDate = (String) session.getAttribute(DATE);
        log.info("clarified date - " + clarifiedDate);
        final String currentIntent = getInitialIntent(session);
        log.info("initial intent: " + currentIntent);
        if (currentIntent == null) return help();
        switch (NatalChartIntent.getByName(currentIntent)) {
            case SUN_SIGN_INTENT://todo add other planets in sign
                return astrologer.respondToSunSign(clarifiedDate);
            case MOON_SIGN_INTENT:
                return astrologer.respondToMoonSign(clarifiedDate);
            case NATAL_CHART_INTENT:
                return astrologer.respondToNatalChart(clarifiedDate);
            default:
                return help();
        }
    }

    private SpeechletResponse respondToBirthTime(Session session, Slot slot) {
        log.info("time - " + slot.getValue());
        final String currentIntent = getInitialIntent(session);
        if (currentIntent == null) return help();
        log.info("initial intent: " + currentIntent);
        switch (NatalChartIntent.getByName(currentIntent)) {
            //todo - can be a sun sign too here (sign borders)
            case MOON_SIGN_INTENT:
                //get a date from session
                return astrologer.respondToMoonSign(slot.getValue());
            case NATAL_CHART_INTENT:
                //get a date from session
                return astrologer.respondToNatalChart(slot.getValue());
            default:
                return help();
        }
    }

    private String getInitialIntent(Session session) {
        return (String) session.getAttribute(INITIAL_INTENT);
    }

    private SpeechletResponse respondToBirthPlace(Session session, Slot daySlot) {
//        GeoLocation response = service.getLocationByName(daySlot.getValue());
        //todo - confirm which one of them
//        System.out.println("Lat " + response.getLat());
//        System.out.println("Long " + response.getLng());
//        return speak(Cards.SPEAK_NATAL_CHART, service.getNatalChart(daySlot.getValue()));
        return astrologer.respondToNatalChart(daySlot.getValue());
    }

    private SpeechletResponse askForBirthDateYear() {
        return ask(Cards.TELL_ME_BIRTH_YEAR);
    }

    private SpeechletResponse askForBirthDate() {
        return ask(Cards.TELL_ME_BIRTH_DAY);
    }

    private SpeechletResponse askForBirthTime() {
        return ask(Cards.TELL_ME_BIRTH_TIME);
    }

    private SpeechletResponse askForBirthPlace() {
        return speak(Cards.TELL_ME_BIRTH_PLACE);
    }

    private void setInitialIntent(Session session, String name) {
        session.setAttribute(INITIAL_INTENT, name);
    }
}
