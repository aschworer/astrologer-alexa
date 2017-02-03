package aschworer.astrologer.alexa.service.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author aschworer
 */
public class GeoLocation {

    private String lat;
    private String lng;
    private String fullName;
    private String name;

    public GeoLocation(String lat, String lng, String fullName) {
        this.lat = lat;
        this.lng = lng;
        this.fullName = fullName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(GeoLocation.class);
    }
}
