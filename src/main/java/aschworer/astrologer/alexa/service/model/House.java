package aschworer.astrologer.alexa.service.model;

import com.google.gson.annotations.SerializedName;

/**
 * @author aschworer on 01-Nov-15.
 */
public enum House implements Characteristic {
    @SerializedName("Ascendant")
    I("House1"),
    II("House2"), III("House3"), IV("House4"), V("House5"), VI("House6"), VII("House7"), VIII("House8"), IX("House9"),
    @SerializedName("Midheaven")
//    X("Midheaven"),
    X("House10"),
    XI("House11"), XII("House12");

    private String string;

    House(String name) {
        string = name;
    }

    public static Characteristic getByString(String str) {
        for (House v : values()) {
            if (v.string.equalsIgnoreCase(str)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return string;
    }

    public String getString() {
        return string;
    }

    public Integer getWeight() {
        switch (this) {
            case I:
                return 7;
            case X:
                return 5;
            default:
//                throw new IllegalArgumentException();
                return 0;//todo throw exception
        }
    }
}
