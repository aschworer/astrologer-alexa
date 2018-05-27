package aschworer.astrologer.alexa.handler;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.handler.responder.charts.*;
import com.amazon.speech.json.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;
import org.slf4j.*;

/**
 * @author aschworer
 */
public class AstrologerSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(AstrologerSpeechlet.class);

    private AlexaResponder alexaResponder = new AlexaResponder();

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
//        log.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
        return alexaResponder.greet();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Intent intent = requestEnvelope.getRequest().getIntent();
//        log.info("onIntent requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
        try {
            SpeechletResponse speechletResponse = alexaResponder.respondToIntent(intent, requestEnvelope.getSession());
            if (speechletResponse.getCard().getTitle().startsWith("TellMe")) {
                requestEnvelope.getSession().setAttribute(SessionDetails.LAST_SPOKEN_CARD, speechletResponse.getCard().getTitle());
                requestEnvelope.getSession().setAttribute(SessionDetails.LAST_SPEECH, ((SimpleCard)speechletResponse.getCard()).getContent());
            }
            return speechletResponse;
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw e;
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
    }
}