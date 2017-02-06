package aschworer.astrologer.alexa.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author aschworer on 01-Nov-15.
 */

public enum Planet implements Characteristic {
    SUN("Sun"), MOON("Moon"), MERCURY("Mercury"), MARS("Mars"), VENUS("Venus"),
    JUPITER("Jupiter"), SATURN("Saturn"), URANUS("Uranus"), NEPTUNE("Neptune"),
    PLUTO("Pluto"), CHIRON("Chiron"), /*ASCENDANT("Ascendant"), MIDHEAVEN("Midheaven"),*/
    LILITH("Lilith"),
    ASC("Asc"),//todo not a planet
    @SerializedName("Asc node")
    ASC_NODE("Asc node");

    private String string;

    Planet(String name) {
        string = name;
    }

    public static Characteristic getByString(String str) {
        for (Planet v : values()) {
            if (v.string.equalsIgnoreCase(str)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (this == ASC) return "Ascendant";
        return string;
    }

    public String getString() {
        return toString();
    }

}
