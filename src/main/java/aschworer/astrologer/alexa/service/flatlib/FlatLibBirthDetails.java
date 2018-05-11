package aschworer.astrologer.alexa.service.flatlib;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author aschworer
 */
@NoArgsConstructor
public class FlatLibBirthDetails {

    @Setter
    @Getter
    private String date;
    @Setter
    @Getter
    private String time;
    @Setter
    @Getter
    private String lng;
    @Setter
    @Getter
    private String lat;
    @Setter
    @Getter
    private String timezone;

    @Override
    public String toString() {
        return "FlatLibBirthDetails{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", lng='" + lng + '\'' +
                ", lat='" + lat + '\'' +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
