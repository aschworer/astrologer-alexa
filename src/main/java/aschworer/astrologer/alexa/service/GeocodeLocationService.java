package aschworer.astrologer.alexa.service;

import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aschworer
 */
public class GeocodeLocationService {

    private static final Logger logger = LoggerFactory.getLogger(GeocodeLocationService.class);

    private static final String URL_GEOCODE =
            "http://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String URL_GEOCODE_ENDING = "&sensor=false";

    public GeoLocation getFirstLocationByName(String locationName) {
        final GeocodeResponse response = getLocationByName(locationName);
        String latitude = response.getResults().get(0).getGeometry().getLocation().getLat()
                .toString();
        String longitude = response.getResults().get(0).getGeometry().getLocation().getLng
                ().toString();
        return new GeoLocation(latitude, longitude, response.getResults().get(0)
                .getFormattedAddress());
    }

    public List<GeoLocation> getLocationsByName(String locationName) {
        final GeocodeResponse response = getLocationByName(locationName);
        List<GeoLocation> list = new ArrayList<>();
        for (GeocoderResult geocoderResult : response.getResults()) {
            GeoLocation geoLocation =
                    new GeoLocation(geocoderResult.getGeometry().getLocation().getLat().toString(),
                            geocoderResult.getGeometry().getLocation().getLng().toString(),
                            geocoderResult.getFormattedAddress());
            list.add(geoLocation);
        }
        return list;
    }

    private GeocodeResponse getLocationByName(String name) {
        InputStreamReader inputStream = null;
        BufferedReader bufferedReader = null;
        String text = "";
        try {
            String line;
            final String urlString = URL_GEOCODE + name + "_" + URL_GEOCODE_ENDING;
            URL url = new URL(urlString.replace(" ", ""));
            logger.debug("google location url: " + url);
            inputStream = new InputStreamReader(url.openStream(), Charset.forName("US-ASCII"));
            bufferedReader = new BufferedReader(inputStream);
            StringBuilder builder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            text = builder.toString();
        } catch (IOException e) {
            // reset text variable to a blank string
            text = "";
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(bufferedReader);
        }
        return new Gson().fromJson(text, GeocodeResponse.class);
    }


    public GeoLocation getCountryByName(String name) {//todo
        return getLocationsByName(name).get(0);
    }


    public Map<String, String> getCountryCoordinates(String place) {
        HashMap<String, String> coordinates = new HashMap<>();
        try {
            final GeoLocation countryLocation = getCountryByName(place);
            coordinates.put("lat", countryLocation.getLat());
            coordinates.put("lng", countryLocation.getLng());
            coordinates.put("fullname", countryLocation.getFullName());
            return coordinates;
        } catch (Exception e) {
            //todo
            return null;
        }
    }

    public class GeoLocation {

        @Getter
        @Setter
        private String lat;
        @Getter
        @Setter
        private String lng;
        @Getter
        @Setter
        private String fullName;
        @Getter
        @Setter
        private String name;

        public GeoLocation(String lat, String lng, String fullName) {
            this.lat = lat;
            this.lng = lng;
            this.fullName = fullName;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(GeoLocation.class);
        }
    }
}