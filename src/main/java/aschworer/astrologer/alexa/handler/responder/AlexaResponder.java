package aschworer.astrologer.alexa.handler.responder;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import aschworer.astrologer.alexa.handler.responder.charts.SpokenCards;

/**
 * @author aschworer
 */
public abstract class AlexaResponder extends Speaker {

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

    public abstract SpeechletResponse respondToCustomIntent(Intent intent, Session session);

    public SpeechletResponse greet() {
        return ask(SpokenCards.WELCOME);
    }

    public SpeechletResponse help() {
        return ask(SpokenCards.HELP);
    }

    public SpeechletResponse stop() {return speakAndFinish(SpokenCards.STOP);}

    public SpeechletResponse cancel() {
        return speakAndFinish(SpokenCards.CANCEL);
    }

}
