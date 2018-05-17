package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;

public class SessionDetails {

    //    private static final String BIRTH_PLACE_FULLNAME = "place_full";
    public static final String INITIAL_INTENT = "initialIntent";
    public static final String BIRTH_DATE = "birthDate";
    public static final String BIRTH_YEAR = "year";
    public static final String BIRTH_PLACE = "place";
    public static final String BIRTH_LAT = "lat";
    public static final String BIRTH_LNG = "lng";
    public static final String BIRTH_TIME = "time";
    public static final String PLANET = "planet";

    private Session session;

    SessionDetails(Session session) {
        this.session = session;
    }

    void setYear(String year) {
        session.setAttribute(BIRTH_YEAR, year);
    }

    boolean isSunSignRequested() {
        return AstrologerIntent.SUN_SIGN_INTENT.equals(getInitialIntent());
    }

    Planet getPlanet() {
        return Planet.getByString((String) session.getAttribute(PLANET));//todo can i just put Planet obj?
    }

    public void setPlanet(String planet) {
        session.setAttribute(PLANET, planet);
    }

    AstrologerIntent getInitialIntent() {
        return AstrologerIntent.getByName((String) session.getAttribute(INITIAL_INTENT));
    }

    void setInitialIntent(AstrologerIntent initialIntent) {
        session.setAttribute(INITIAL_INTENT, initialIntent.getName());
    }

    boolean isInitialIntentPresent() {
        return session.getAttribute(INITIAL_INTENT) != null;
    }

    String getBirthDate() {
        return (String) session.getAttribute(BIRTH_DATE);
    }

    void setBirthDate(String day) {
        session.setAttribute(BIRTH_DATE, day);
    }

    String getBirthYear() {
        return (String) session.getAttribute(BIRTH_YEAR);
    }

    String getBirthPlace() {
        return (String) session.getAttribute(BIRTH_PLACE);
    }

    void setBirthPlace(String place) {
        session.setAttribute(BIRTH_PLACE, place);
    }

    String getBirthLat() {
        return (String) session.getAttribute(BIRTH_LAT);
    }

    void setBirthLat(String lat) {
        session.setAttribute(BIRTH_LAT, lat);
    }

    String getBirthLng() {
        return (String) session.getAttribute(BIRTH_LNG);
    }

    void setBirthLng(String lng) {
        session.setAttribute(BIRTH_LAT, lng);
    }
}

