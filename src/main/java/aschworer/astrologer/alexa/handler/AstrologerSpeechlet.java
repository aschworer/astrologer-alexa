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

    private static String intentToString(Intent intent) {
        StringBuilder intentStr = new StringBuilder(intent.getName());
        intentStr.append(" (");
        for (String slotKey : intent.getSlots().keySet()) {
            intentStr.append(slotKey).append(": ").append(intent.getSlot(slotKey).getValue()).append("; ");
        }
        return intentStr.append(") ").toString();
    }

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        log.info("onSessionStarted locale={} requestId={}, sessionId={}",
                requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId(), requestEnvelope.getRequest().getLocale());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        log.info("onLaunch locale={} requestId={}, sessionId={}",
                requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId(), requestEnvelope.getRequest().getLocale());
        SpeechletResponse greet = alexaResponder.greet();
        SessionDetails sessionDetails = new SessionDetails(requestEnvelope.getSession());
        sessionDetails.setLastSpokenCard(greet.getCard().getTitle());
        sessionDetails.setLastSpokenSpeech(((SimpleCard) greet.getCard()).getContent());
        return greet;
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope) {
        Intent intent = requestEnvelope.getRequest().getIntent();
        log.info("onIntent locale={} requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId(), requestEnvelope.getRequest().getLocale());
        log.info("Intent {}", intentToString(intent));
        SessionDetails sessionDetails = new SessionDetails(requestEnvelope.getSession());
        log.info("Session on request - " + sessionDetails);
        try {
            SpeechletResponse speechletResponse = alexaResponder.respondToIntent(intent, requestEnvelope.getSession());
            String responseCardTitle = speechletResponse.getCard().getTitle();
            String responseSpeech = ((SimpleCard) speechletResponse.getCard()).getContent();
            sessionDetails.setLastSpokenCard(responseCardTitle);
            sessionDetails.setLastSpokenSpeech(responseSpeech);
            if (responseCardTitle != null && responseCardTitle.startsWith("TellMe")) {
                sessionDetails.setLastTellMeCard(responseCardTitle);
                sessionDetails.setLastTellMeSpeech(responseSpeech);
            }
            log.info("Session on response - " + sessionDetails);
            return speechletResponse;
        } catch (Exception e) {
            log.error("Exception: ", e);
            throw e;
        }
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope) {
        log.info("onSessionEnded locale={} requestId={}, sessionId={}",
                requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId(), requestEnvelope.getRequest().getLocale());
    }
}