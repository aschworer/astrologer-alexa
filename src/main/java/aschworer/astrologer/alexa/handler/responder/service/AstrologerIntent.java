package aschworer.astrologer.alexa.handler.responder.service;

/**
 * @author aschworer
 */
public enum AstrologerIntent {
    SUN_SIGN_INTENT("SunSignIntent"),
    MOON_SIGN_INTENT("MoonSignIntent"),
    NATAL_CHART_INTENT("NatalChartIntent"),
    BIRTH_DAY_INTENT("BirthDayIntent"),
    BIRTH_YEAR_INTENT("BirthYearIntent"),
    YES_INTENT("AMAZON.YesIntent"),
    NO_INTENT("AMAZON.NoIntent"),
    BIRTH_TIME_INTENT("BirthTimeIntent"),
    BIRTH_PLACE_INTENT("BirthPlaceIntent"),
    NA("NA");

    private String name;

    AstrologerIntent(String name) {
        this.name = name;
    }

    public static AstrologerIntent getByName(String name) {
        for (AstrologerIntent v : values()) {
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
