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
    public static final String BIRTH_PLACE_CONFIRMED = "birthPlaceConfirmed";
    public static final String LAST_TELLME_CARD = "lastTellMeCard";
    public static final String LAST_TELLME_SPEECH = "lastTellMeSpeech";
    public static final String LAST_SPOKEN_CARD = "lastSpokenCard";
    public static final String LAST_SPOKEN_SPEECH = "lastSpokenSpeech";
    public static final String UNKNOWN = "Unknown";

    private Session session;

    public SessionDetails(Session session) {
        this.session = session;
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

    public void setLastTellMeSpeech(String lastTellMeSpeech) {
        session.setAttribute(LAST_TELLME_SPEECH, lastTellMeSpeech);
    }

    public String getLastTellMeCard() {
        return (String) session.getAttribute(LAST_TELLME_CARD);
    }

    public void setLastTellMeCard(String lastTellMeCard) {
        session.setAttribute(LAST_TELLME_CARD, lastTellMeCard);
    }

    public String getLastSpokenCard() {
        return (String) session.getAttribute(LAST_SPOKEN_CARD);
    }

    public void setLastSpokenCard(String lastSpokenCard) {
        session.setAttribute(LAST_SPOKEN_CARD, lastSpokenCard);
    }

    public String getLastSpokenSpeech() {
        return (String) session.getAttribute(LAST_SPOKEN_SPEECH);
    }

    public void setLastSpokenSpeech(String lastSpokenSpeech) {
        session.setAttribute(LAST_SPOKEN_SPEECH, lastSpokenSpeech);
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
        return SpokenCards.WHATS_BIRTH_YEAR.equals(session.getAttribute(LAST_SPOKEN_CARD));
    }

    public boolean isAskingForBirthDay() {
        return SpokenCards.TELL_ME_BIRTH_DAY.equals(session.getAttribute(LAST_TELLME_CARD)) || SpokenCards.WHATS_BIRTH_YEAR.equals(session.getAttribute(LAST_TELLME_CARD));
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

    void setBirthYear(String year) {
        session.setAttribute(BIRTH_YEAR, year);
    }

    public String getBirthPlace() {
        return (String) session.getAttribute(BIRTH_PLACE);
    }

    public void setBirthPlace(String place) {
        session.setAttribute(BIRTH_PLACE, place);
    }

    public void setBirthPlaceConfirmed() {
        session.setAttribute(BIRTH_PLACE_CONFIRMED, Boolean.TRUE);
    }

    public Boolean isBirthDateConfirmed() {
        Object attribute = session.getAttribute(BIRTH_DATE_CONFIRMED);
        if (attribute == null) {
            return Boolean.FALSE;
        }
        return (Boolean) attribute;
    }

    public void setBirthDateConfirmed() {
        session.setAttribute(BIRTH_DATE_CONFIRMED, Boolean.TRUE);
    }

    public Boolean isBirthPlaceConfirmed() {
        Object attribute = session.getAttribute(BIRTH_PLACE_CONFIRMED);
        if (attribute == null) {
            return Boolean.FALSE;
        }
        return (Boolean) attribute;
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

    public void clearUserInput(){
        session.removeAttribute(BIRTH_PLACE_FULLNAME);
        session.removeAttribute(BIRTH_DATE);
        session.removeAttribute(BIRTH_YEAR);
        session.removeAttribute(BIRTH_PLACE);
        session.removeAttribute(BIRTH_LAT);
        session.removeAttribute(BIRTH_LNG);
        session.removeAttribute(BIRTH_TIMEZONE_OFFSET);
        session.removeAttribute(BIRTH_TIME);
        session.removeAttribute(PLANET);
        session.removeAttribute(BIRTH_DATE_CONFIRMED);
        session.removeAttribute(BIRTH_PLACE_CONFIRMED);
        session.removeAttribute(LAST_TELLME_CARD);
    }

    @Override
    public String toString() {
        return "Session: \n" + "intent: " + getInitialIntent() + "\n" +
                "last question: " + ((getLastTellMeSpeech() == null) ? "none" : getLastTellMeSpeech()) + "\n" +
                "last spoken speech: " + ((getLastSpokenSpeech() == null) ? "none" : getLastSpokenSpeech()) + "\n" +
                "b-date: " + getBirthDate() + "\n" +
                "b-date confirmed: " + isBirthDateConfirmed() + "\n" +
                "b-year: " + getBirthYear() + "\n" +
                "b-time: " + getBirthTime() + "\n" +
                "b-place: " + getBirthPlace() + "\n" +
                "b-place confirmed: " + isBirthPlaceConfirmed() + "\n" +
                "full b-place: " + getFullBirthPlace() + "\n" +
                "lat: " + getBirthLat() + "\n" +
                "lng: " + getBirthLng() + "\n" +
                "planet: " + getPlanet();
    }

}

