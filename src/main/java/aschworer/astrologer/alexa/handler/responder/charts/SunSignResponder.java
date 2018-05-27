package aschworer.astrologer.alexa.handler.responder.charts;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;
import org.slf4j.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;

public class SunSignResponder extends PlanetInSignResponder {
    private static final Logger log = LoggerFactory.getLogger(SunSignResponder.class);

    public SunSignResponder(SessionDetails sessionDetails) {
        super(sessionDetails);
        sessionDetails.setInitialIntent(AstrologerIntent.SUN_SIGN_INTENT);
    }

    @Override
    public SpeechletResponse respondToInitialIntent(SessionDetails session) {
        session.setPlanet(Planet.SUN.getString());
        return super.respondToInitialIntent(session);
    }

    public SpeechletResponse respondToBirthDay(SessionDetails session) {
        return ask(DOUBLE_CHECK_DATE, String.format(SAY_AS_DATE, formatNoYear(session.getBirthDate())));
    }
}
