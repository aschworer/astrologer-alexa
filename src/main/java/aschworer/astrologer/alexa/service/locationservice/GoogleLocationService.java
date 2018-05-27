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

    public static void main(String[] args) throws Exception {
        new GoogleLocationService().getFirstLocationByName("Moscow");
    }

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
        return getLocationsByName(locationName).get(0);
    }

    public List<Location> getLocationsByName(String locationName) throws Exception {
        GeocodingResult[] results = GeocodingApi.geocode(context, locationName).await();
        List<Location> list = new ArrayList<>();

        for (GeocodingResult geocoderResult : results) {
            String latitude = String.valueOf(geocoderResult.geometry.location.lat);
            String longitude = String.valueOf(geocoderResult.geometry.location.lng);
            TimeZone timezone = getTimezone(Double.parseDouble(latitude), Double.parseDouble(longitude));
            Location location = new Location(latitude, longitude, geocoderResult.formattedAddress, getGMTOffset(timezone),
                    timezone.getDisplayName());
            list.add(location);
        }
        return list;
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
