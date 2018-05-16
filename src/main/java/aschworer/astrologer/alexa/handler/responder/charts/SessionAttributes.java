package aschworer.astrologer.alexa.handler.responder.charts;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author ASC
 */
public interface SessionAttributes {
    String INITIAL_INTENT = "initial_intent";
    String BIRTH_DATE = "date";
    String BIRTH_YEAR = "year";
    String BIRTH_PLACE = "place";
    //    public static final String BIRTH_PLACE_FULLNAME = "place_full";
    String BIRTH_LAT = "lat";
    String BIRTH_LNG = "lng";
    String BIRTH_TIME = "time";
    String PLANET = "planet";
    String CURRENT_YEAR = new SimpleDateFormat("yyyy").format(new Date());
}
