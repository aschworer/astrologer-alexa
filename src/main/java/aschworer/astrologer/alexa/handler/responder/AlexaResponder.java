package aschworer.astrologer.alexa.handler.responder;

import aschworer.astrologer.alexa.handler.responder.charts.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import static aschworer.astrologer.alexa.handler.responder.charts.AstrologerIntent.*;

/**
 * @author aschworer
 */
public class AlexaResponder extends Speaker {

    private static final Logger log = LoggerFactory.getLogger(AlexaResponder.class);
    private AstrologerResponder responder;

    public SpeechletResponse respondToIntent(Intent intent, Session session) {
        switch (AlexaIntent.getByName(intent.getName())) {
            case AMAZON_HELP_INTENT:
                return help();
            case AMAZON_STOP_INTENT:
                return stop();
            case AMAZON_CANCEL_INTENT:
                return cancel();
            default:
                return respondToCustomIntent(intent, session);
        }
    }

    private SpeechletResponse respondToCustomIntent(Intent intent, Session session) {
        log.info(intent.getName());
        SessionDetails sessionDetails = new SessionDetails(session);
        AstrologerIntent currentIntent = AstrologerIntent.getByName(intent.getName());
        AstrologerIntent initialIntent = AstrologerIntent.getByName(sessionDetails.getInitialIntent().getName());
        if (SUN_SIGN_INTENT.equals(currentIntent) || SUN_SIGN_INTENT.equals(initialIntent)) {
            responder = new SunSignResponder(sessionDetails);
        } else if (PLANET_SIGN_INTENT.equals(currentIntent) || PLANET_SIGN_INTENT.equals(initialIntent)) {
            responder = new PlanetInSignResponder(sessionDetails);
        } else if (FULL_CHART_INTENT.equals(currentIntent) || FULL_CHART_INTENT.equals(initialIntent)) {
            responder = new ChartResponder(sessionDetails);
        }
        if (responder == null) {
            return greet();
        } else {
            return responder.handle(intent, sessionDetails);
        }

    }

    public SpeechletResponse greet() {
        return repeatedSpeech(SpokenCards.WELCOME);
    }

    public SpeechletResponse help() {
        return repeatedSpeech(SpokenCards.HELP);
    }

    public SpeechletResponse stop() {
        return speakAndFinish(SpokenCards.STOP);
    }

    public SpeechletResponse cancel() {
        return speakAndFinish(SpokenCards.CANCEL);
    }

}
