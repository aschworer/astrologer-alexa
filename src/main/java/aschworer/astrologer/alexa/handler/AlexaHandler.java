package aschworer.astrologer.alexa.handler;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author aschworer
 */
public class AlexaHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<>();

    static {
        /*
         * Place relevant Ids here to make the skill work
         */
        supportedApplicationIds.add("");
    }

    public AlexaHandler() {
        super(new NatalChartSpeechlet(), supportedApplicationIds);
    }
}
