package aschworer.astrologer.alexa.service.locationservice;

import lombok.*;
import org.apache.commons.lang3.builder.*;

/**
 * @author aschworer
 */
public class Location {

    @Getter
    private String lat;
    @Getter
    private String lng;
    @Getter
    private String fullName;
    @Getter
    private String timezoneOffset;
    @Getter
    private String timezoneName;

    public Location(String lat, String lng, String fullName, String timezoneOffset, String timezoneName) {
        this.lat = lat;
        this.lng = lng;
        this.fullName = fullName;
        this.timezoneOffset = timezoneOffset;
        this.timezoneName = timezoneName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(Location.class);
    }
}
