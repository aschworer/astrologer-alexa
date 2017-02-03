package aschworer.astrologer.alexa.handler.responder.service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ASC
 */
public class SessionConstants {
    public static final String INITIAL_INTENT = "initial_intent";
    public static final String BIRTH_DATE = "date";
    public static final String BIRTH_YEAR = "year";
    public static final String BIRTH_PLACE = "place";
    //    public static final String BIRTH_PLACE_FULLNAME = "place_full";
    public static final String BIRTH_LAT = "lat";
    public static final String BIRTH_LNG = "lng";
    public static final String BIRTH_TIME = "time";
    public static final String CURRENT_YEAR = new SimpleDateFormat("yyyy").format(new Date());


}
