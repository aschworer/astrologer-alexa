package aschworer.astrologer.alexa.handler;

import aschworer.astrologer.alexa.handler.responder.AlexaResponder;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import aschworer.astrologer.alexa.handler.responder.service.NatalChartAlexaResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aschworer
 */
public class NatalChartSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(NatalChartSpeechlet.class);

    private AlexaResponder alexaResponder = new NatalChartAlexaResponder();

    @Override
    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", sessionStartedRequest.getRequestId(), session.getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", launchRequest.getRequestId(), session.getSessionId());
        return alexaResponder.greet();

    }

    @Override
    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        Intent intent = intentRequest.getIntent();
        log.info("onSessionStarted requestId={}, sessionId={}", intentRequest.getRequestId(), session.getSessionId());
        return alexaResponder.respondToIntent(intent, session);
    }

    @Override
    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", sessionEndedRequest.getRequestId(), session.getSessionId());
    }
}