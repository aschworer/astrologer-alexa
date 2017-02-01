package aschworer.astrologer.alexa.handler.responder;

/**
 * @author aschworer
 */
public enum AlexaIntent {
    AMAZON_STOP_INTENT("AMAZON.StopIntent"),
    AMAZON_CANCEL_INTENT("AMAZON.CancelIntent"),
    AMAZON_HELP_INTENT("AMAZON.HelpIntent"),

    NA("NA");

    private String name;

    AlexaIntent(String name) {
        this.name = name;
    }

    public static AlexaIntent getByName(String name) {
        for (AlexaIntent v : values()) {
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
