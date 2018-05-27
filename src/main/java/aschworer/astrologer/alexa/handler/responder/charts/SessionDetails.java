package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;

public class SessionDetails {

    public static final String BIRTH_PLACE_FULLNAME = "place_full";
    public static final String INITIAL_INTENT = "initialIntent";
    public static final String BIRTH_DATE = "birthDate";
    public static final String BIRTH_YEAR = "year";
    public static final String BIRTH_PLACE = "place";
    public static final String BIRTH_LAT = "lat";
    public static final String BIRTH_LNG = "lng";
    public static final String BIRTH_TIMEZONE_OFFSET = "timeZoneOffset";
    public static final String BIRTH_TIME = "time";
    public static final String PLANET = "planet";
    public static final String BIRTH_DATE_CONFIRMED = "birthDateConfirmed";
    public static final String LAST_TELLME_CARD = "lastTellMeCard";
    public static final String LAST_TELLME_SPEECH = "lastTellMeSpeech";
    public static final String LAST_SPOKEN_CARD = "lastSpokenCard";
    public static final String LAST_SPOKEN_SPEECH = "lastSpokenSpeech";

    private Session session;

    public SessionDetails(Session session) {
        this.session = session;
    }

    void setYear(String year) {
        session.setAttribute(BIRTH_YEAR, year);
    }

    public Planet getPlanet() {
        return Planet.getByString((String) session.getAttribute(PLANET));//todo can i just put Planet obj?
    }

    public void setPlanet(String planet) {
        session.setAttribute(PLANET, planet);
    }

    public AstrologerIntent getInitialIntent() {
        return AstrologerIntent.getByName((String) session.getAttribute(INITIAL_INTENT));
    }

    public void setInitialIntent(AstrologerIntent initialIntent) {
        session.setAttribute(INITIAL_INTENT, initialIntent.getName());
    }

    public String getLastTellMeSpeech() {
        return (String) session.getAttribute(LAST_TELLME_SPEECH);
    }

    public String getLastTellMeCard() {
        return (String) session.getAttribute(LAST_TELLME_CARD);
    }

    public String getLastSpokenCard() {
        return (String) session.getAttribute(LAST_SPOKEN_CARD);
    }

    public String getLastSpokenSpeech() {
        return (String) session.getAttribute(LAST_SPOKEN_SPEECH);
    }

    public String getBirthTimeZoneOffset() {
        return (String) session.getAttribute(BIRTH_TIMEZONE_OFFSET);
    }

    public void setBirthTimeZoneOffset(String timezoneOffset) {
        session.setAttribute(BIRTH_TIMEZONE_OFFSET, timezoneOffset);
    }

    public boolean isAskingForBirthPlace() {
        return SpokenCards.TELL_ME_BIRTH_PLACE.equals(session.getAttribute(LAST_TELLME_CARD));
    }

    public boolean isAskingForBirthTime() {
        return SpokenCards.TELL_ME_BIRTH_TIME.equals(session.getAttribute(LAST_TELLME_CARD));
    }

    public boolean isAskingForBirthYear() {
        return SpokenCards.TELL_ME_BIRTH_YEAR.equals(session.getAttribute(LAST_TELLME_CARD));
    }

    public String getBirthDate() {
        return (String) session.getAttribute(BIRTH_DATE);
    }

    public void setBirthDate(String day) {
        session.setAttribute(BIRTH_DATE, day);
    }

    public String getBirthTime() {
        return (String) session.getAttribute(BIRTH_TIME);
    }

    public void setBirthTime(String time) {
        session.setAttribute(BIRTH_TIME, time);
    }

    public String getBirthYear() {
        return (String) session.getAttribute(BIRTH_YEAR);
    }

    public String getBirthPlace() {
        return (String) session.getAttribute(BIRTH_PLACE);
    }

    public void setBirthPlace(String place) {
        session.setAttribute(BIRTH_PLACE, place);
    }

    public String getFullBirthPlace() {
        return (String) session.getAttribute(BIRTH_PLACE_FULLNAME);
    }

    public void setBirthPlaceFull(String place) {
        session.setAttribute(BIRTH_PLACE_FULLNAME, place);
    }

    public String getBirthLat() {
        return (String) session.getAttribute(BIRTH_LAT);
    }

    public void setBirthLat(String lat) {
        session.setAttribute(BIRTH_LAT, lat);
    }

    public String getBirthLng() {
        return (String) session.getAttribute(BIRTH_LNG);
    }

    public void setBirthLng(String lng) {
        session.setAttribute(BIRTH_LNG, lng);
    }

    public Boolean isBirthDateConfirmed() {
        Object attribute = session.getAttribute(BIRTH_DATE_CONFIRMED);
        return (attribute == null) ? Boolean.FALSE : (Boolean) attribute;
    }

    public void setBirthDateConfirmed() {
        session.setAttribute(BIRTH_DATE_CONFIRMED, Boolean.TRUE);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Session: \n");
        result.append("intent: ").append(getInitialIntent()).append("\n");
        result.append("last speech: ").append((getLastTellMeCard() == null) ? "none" : getLastTellMeCard()).append("\n");
        result.append("b-date: ").append(getBirthDate()).append("\n");
        result.append("b-date confirmed: ").append(isBirthDateConfirmed()).append("\n");
        result.append("b-year: ").append(getBirthYear()).append("\n");
        result.append("b-time: ").append(getBirthTime()).append("\n");
        result.append("b-place: ").append(getBirthPlace()).append("\n");
        result.append("full b-place: ").append(getFullBirthPlace()).append("\n");
        result.append("lat: ").append(getBirthLat()).append("\n");
        result.append("lng: ").append(getBirthLng()).append("\n");
        result.append("planet: ").append(getPlanet());

        return result.toString();
    }


}

