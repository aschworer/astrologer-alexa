package aschworer.astrologer.alexa.service.model;

/**
 * @author aschworer on 01-Nov-15.
 */
public enum Sign {
    ARIES("Aries"), TAURUS("Taurus"), GEMINI("Gemini"), CANCER("Cancer"), LEO("Leo"), VIRGO("Virgo"),
    LIBRA("Libra"), SCORPIO("Scorpio"), SAGITTARIUS("Sagittarius"), CAPRICORN("Capricorn"), AQUARIUS("Aquarius"), PISCES("Pisces"), VARY("Vary");

    private String string;

    Sign(String name) {
        string = name;
    }

    public static Sign getByString(String str) {
        for (Sign v : values()) {
            if (v.toString().equalsIgnoreCase(str)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return string;
    }

}
