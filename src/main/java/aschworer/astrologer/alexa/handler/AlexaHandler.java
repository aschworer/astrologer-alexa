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
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.40b6b5c2-9cdd-4613-80d1-dbaecb7413d0");
    }

    public AlexaHandler() {
        super(new AstrologerSpeechlet(), supportedApplicationIds);
    }
}
