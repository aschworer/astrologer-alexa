package aschworer.astrologer.alexa.handler;

import aschworer.astrologer.alexa.handler.responder.AlexaResponder;
import com.amazon.speech.json.SpeechletRequestEnvelope;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import aschworer.astrologer.alexa.handler.responder.service.NatalChartAlexaResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aschworer
 */
public class NatalChartSpeechlet implements SpeechletV2 {

    private static final Logger log = LoggerFactory.getLogger(NatalChartSpeechlet.class);

    private AlexaResponder alexaResponder = new NatalChartAlexaResponder();

    @Override
    public void onSessionStarted(SpeechletRequestEnvelope<SessionStartedRequest> requestEnvelope) {
        log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(SpeechletRequestEnvelope<LaunchRequest> requestEnvelope) {
        log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
        return alexaResponder.greet();
    }

    @Override
    public SpeechletResponse onIntent(SpeechletRequestEnvelope<IntentRequest> requestEnvelope)  {
        Intent intent = requestEnvelope.getRequest().getIntent();
        log.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
        return alexaResponder.respondToIntent(intent, requestEnvelope.getSession());
    }

    @Override
    public void onSessionEnded(SpeechletRequestEnvelope<SessionEndedRequest> requestEnvelope)  {
        log.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest().getRequestId(), requestEnvelope.getSession().getSessionId());
    }
}