package aschworer.astrologer.alexa.handler.responder;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import aschworer.astrologer.alexa.handler.responder.charts.SpokenCards;

/**
 * @author aschworer
 */
public abstract class StandardAlexaResponder extends Speaker implements AlexaResponder {

    @Override
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

    @Override
    public SpeechletResponse greet() {
        return ask(SpokenCards.WELCOME);
    }

    @Override
    public SpeechletResponse help() {
        return ask(SpokenCards.HELP);
    }

    @Override
    public SpeechletResponse stop() {return speakAndFinish(SpokenCards.STOP);}

    @Override
    public SpeechletResponse cancel() {
        return speakAndFinish(SpokenCards.CANCEL);
    }

}
