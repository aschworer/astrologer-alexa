package aschworer.astrologer.alexa.handler.responder;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import aschworer.astrologer.alexa.service.model.CharacteristicInSign;
import aschworer.astrologer.alexa.service.model.NatalChart;
import aschworer.astrologer.alexa.service.model.Planet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public abstract class Speaker {

    private static final Logger log = LoggerFactory.getLogger(StandardAlexaResponder.class);
    private ResourceBundle messages = ResourceBundle.getBundle("messages");

    protected SpeechletResponse ask(String cardName, String... args) {
        return speak(cardName, MessageFormat.format(messages.getString(cardName), args), false);
    }

    protected SpeechletResponse ask(String cardName) {
        return speak(cardName, messages.getString(cardName), false);
    }

    protected SpeechletResponse speak(String cardName, String... args) {
        return speak(cardName, MessageFormat.format(messages.getString(cardName), args), true);
    }

    private SpeechletResponse speak(String cardName, String speechText, boolean end) {
        SimpleCard card = new SimpleCard();
        card.setTitle(cardName);
        card.setContent(speechText);
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml("<speak>" + speechText + "</speak>");
        log.info("return speech " + speechText);
        if (end) {
            return SpeechletResponse.newTellResponse(speech, card);
        } else {
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);
            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }
    }

    protected String getNatalChartAsString(NatalChart natalChart) {
        StringBuilder natalChartSpeech = new StringBuilder();
        for (CharacteristicInSign planetInSign : natalChart.getPlanets()) {
            if (planetInSign.getCharacteristic().equals(Planet.SUN.getString()) || planetInSign.getCharacteristic().equals(Planet.MOON.getString()))
                natalChartSpeech.append("The ");
            natalChartSpeech.append(planetInSign.getCharacteristic());
            natalChartSpeech.append(" is in ");
            natalChartSpeech.append(planetInSign.getSign());
            natalChartSpeech.append("; ");
        }
        return natalChartSpeech.toString();
    }
}
