package aschworer.astrologer.alexa.handler.responder;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * @author aschworer
 */
public interface AlexaResponder {

    SpeechletResponse greet();

    SpeechletResponse respondToIntent(Intent intent, Session session);

    SpeechletResponse help();

    SpeechletResponse stop();

    SpeechletResponse cancel();

}
