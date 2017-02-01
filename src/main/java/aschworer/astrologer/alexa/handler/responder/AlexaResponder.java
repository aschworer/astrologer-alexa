package aschworer.astrologer.alexa.handler.responder;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * @author aschworer
 */
public interface AlexaResponder {

    SpeechletResponse greet() throws SpeechletException;

    SpeechletResponse respondToIntent(Intent intent, Session session) throws SpeechletException;

    SpeechletResponse help() throws SpeechletException;

    SpeechletResponse stop() throws SpeechletException;

    SpeechletResponse cancel() throws SpeechletException;

}
