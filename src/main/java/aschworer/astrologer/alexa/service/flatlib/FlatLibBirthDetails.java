package aschworer.astrologer.alexa.service.flatlib;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.*;

/**
 * @author aschworer
 */
@NoArgsConstructor
public class FlatLibBirthDetails {

    public static final DateTimeFormatter FLATLIB_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public static final DateTimeFormatter FLATLIB_TIME_FORMATTER = DateTimeFormatter.ISO_TIME.withResolverStyle(ResolverStyle.STRICT);

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
