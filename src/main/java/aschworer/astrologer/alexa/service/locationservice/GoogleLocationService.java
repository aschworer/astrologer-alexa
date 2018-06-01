package aschworer.astrologer.alexa.service.locationservice;

import com.google.maps.*;
import com.google.maps.model.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author aschworer
 */
public class GoogleLocationService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleLocationService.class);

    private GeoApiContext context = new GeoApiContext.Builder().apiKey(ResourceBundle.getBundle("servicekeys").getString("google.service.key")).build();

//    public static void main(String[] args) throws Exception {
//        new GoogleLocationService().getFirstLocationByName("Boston");
//    }

    private static String getGMTOffset(TimeZone tz) {
        long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
        long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours);
        String timeZoneGMTOffset = String.format("%02d:%02d", hours, minutes);
        if (!timeZoneGMTOffset.startsWith("-")) {
            timeZoneGMTOffset = "+" + timeZoneGMTOffset;
        }
        return timeZoneGMTOffset;
    }

    public Location getFirstLocationByName(String locationName) throws Exception {
        List<Location> locationsByName = getLocationsByName(locationName);
        if (!locationsByName.isEmpty()) {
            return locationsByName.get(0);
        } else {
            logger.error("Location " + locationName + " not found.");
            throw new Exception();
        }
    }

    private List<Location> getLocationsByName(String locationName) throws Exception {
        GeocodingResult[] results = GeocodingApi.geocode(context, locationName).await();
        List<Location> list = new ArrayList<>();
        for (GeocodingResult place : results) {
            String latitude = String.valueOf(place.geometry.location.lat);
            String longitude = String.valueOf(place.geometry.location.lng);
            TimeZone timezone = getTimezone(Double.parseDouble(latitude), Double.parseDouble(longitude));
            for (AddressComponentType addressComponentType : place.addressComponents[0].types) {
                if (addressComponentType == AddressComponentType.COUNTRY || addressComponentType == AddressComponentType.LOCALITY) {
                    list.add(new Location(latitude, longitude, getFullName(place), getGMTOffset(timezone), timezone.getDisplayName()));
                }
            }
        }
        return list;
    }

    private String getFullName(GeocodingResult place) {
        String city = null;
        String country = "";
        for (AddressComponent addressComponent : place.addressComponents) {
            for (AddressComponentType addressComponentType : addressComponent.types) {
                if (addressComponentType == AddressComponentType.LOCALITY) {
                    city = addressComponent.shortName;
                }
                if (addressComponentType == AddressComponentType.COUNTRY) {
                    country = addressComponent.longName;
                }
            }
        }
        return (city != null) ? city + ", in " + country : country;
    }

    private TimeZone getTimezone(double lat, double lng) throws Exception {
        TimeZone tz = null;
        PendingResult<TimeZone> result = TimeZoneApi.getTimeZone(context, new LatLng(lat, lng));
        try {
            tz = result.await();
        } catch (Exception e) {
            logger.error("error", e);
            e.printStackTrace();
        }
        return tz;
    }
}
