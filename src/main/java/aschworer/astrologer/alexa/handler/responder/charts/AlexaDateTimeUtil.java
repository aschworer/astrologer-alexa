package aschworer.astrologer.alexa.handler.responder.charts;

import java.time.*;
import java.time.format.*;

public class AlexaDateTimeUtil {

    private static final DateTimeFormatter ALEXA_DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
    private static final DateTimeFormatter ALEXA_TIME_FORMATTER = DateTimeFormatter.ISO_TIME.withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter ALEXA_TIME_FORMATTER2 = DateTimeFormatter.ofPattern("HHmm").withResolverStyle(ResolverStyle.STRICT);

    public static LocalDate parseDate(String date) throws AlexaDateTimeException {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date, ALEXA_DATE_FORMATTER);
        } catch (Exception e) {
            throw new AlexaDateTimeException(SpokenCards.INVALID_DATE);
        }
        if (parsedDate.getYear() < 1550 || parsedDate.getYear() > 2649) {
            throw new AlexaDateTimeException(SpokenCards.INVALID_DATE_RANGE);
        }
        return parsedDate;
    }

    public static void main(String[] args) {
        LocalDate parsedDate = LocalDate.parse("1011-11-20", ALEXA_DATE_FORMATTER);
        System.out.println(parsedDate);
    }

    static LocalTime parseTime(String time) throws AlexaDateTimeException {
        try {
            LocalTime parsedTime;
            if ("EV".equalsIgnoreCase(time)) {
                parsedTime = LocalTime.of(21, 0);
            } else if ("MO".equalsIgnoreCase(time)) {
                parsedTime = LocalTime.of(9, 0);
            } else if ("NI".equalsIgnoreCase(time)) {
                parsedTime = LocalTime.of(3, 0);
            } else if ("AF".equalsIgnoreCase(time)) {
                parsedTime = LocalTime.of(15, 0);
            } else {
                parsedTime = LocalTime.parse(time, ALEXA_TIME_FORMATTER);
            }
            return parsedTime;
        } catch (Exception e) {
            //try another pattern
            try {
                return LocalTime.parse(time, ALEXA_TIME_FORMATTER2);
            } catch (Exception ex) {
                throw new AlexaDateTimeException(SpokenCards.INVALID_TIME);
            }
        }
    }

    static boolean withinAYearFromNow(LocalDate date) {
        LocalDate withinAYear = LocalDate.of(LocalDate.now().getYear() + 1, LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth());
        return (date.isBefore(withinAYear) && date.isAfter(LocalDate.now())) || date.isEqual(LocalDate.now());
    }

    static String replaceYear(String slotValue, String date) {
        if (slotValue != null && !SessionDetails.UNKNOWN.equalsIgnoreCase(slotValue)) {
            return slotValue + date.substring(date.indexOf("-"), date.length());
        }
        return date;
    }
}
