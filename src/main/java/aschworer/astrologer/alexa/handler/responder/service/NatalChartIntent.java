package aschworer.astrologer.alexa.handler.responder.service;

/**
 * @author aschworer
 */
public enum NatalChartIntent {
    SUN_SIGN_INTENT("SunSignIntent"),
    MOON_SIGN_INTENT("MoonSignIntent"),
    NATAL_CHART_INTENT("NatalChartIntent"),
    BIRTH_DAY_INTENT("BirthDayIntent"),
    BIRTH_YEAR_INTENT("BirthYearIntent"),
    CONFIRM_DATE_INTENT("ConfirmDateIntent"),
    DENY_DATE_INTENT("DenyDateIntent"),
    BIRTH_TIME_INTENT("BirthTimeIntent"),
    BIRTH_PLACE_INTENT("BirthPlaceIntent"),
    NA("NA");

    private String name;

    NatalChartIntent(String name) {
        this.name = name;
    }

    public static NatalChartIntent getByName(String name) {
        for (NatalChartIntent v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }
        return NA;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
