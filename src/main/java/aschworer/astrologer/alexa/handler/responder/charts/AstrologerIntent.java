package aschworer.astrologer.alexa.handler.responder.charts;

/**
 * @author aschworer
 */
public enum AstrologerIntent {
    SUN_SIGN_INTENT("SunSignIntent"),
    PLANET_SIGN_INTENT("PlanetSignIntent"),
    FULL_CHART_INTENT("FullChartIntent"),
    BIRTH_DAY_INTENT("BirthDayIntent"),
    BIRTH_YEAR_OR_TIME_INTENT("BirthYearIntent"),
    YES_INTENT("AMAZON.YesIntent"),
    NO_INTENT("AMAZON.NoIntent"),
    BIRTH_TIME_INTENT("BirthTimeIntent"),
    BIRTH_PLACE_INTENT("BirthPlaceIntent"),
    I_DONT_KNOW_INTENT("IDontKnowIntent"),
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
