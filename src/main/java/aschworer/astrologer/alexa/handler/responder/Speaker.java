package aschworer.astrologer.alexa.handler.responder;

import aschworer.astrologer.model.Characteristic;
import aschworer.astrologer.model.CharacteristicInSign;
import aschworer.astrologer.model.House;
import aschworer.astrologer.model.NatalChart;
import aschworer.astrologer.model.Planet;
import aschworer.astrologer.model.Sign;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public abstract class Speaker {

    private static final Logger log = LoggerFactory.getLogger(StandardAlexaResponder.class);
    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};
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
        StringBuilder variations = new StringBuilder();
        StringBuilder natalChartSpeech = new StringBuilder();
        for (CharacteristicInSign planetInSign : natalChart.getCharacteristicsInSigns()) {

//            String characteristic = planetInSign.getCharacteristic();
//            if (House.getByString(characteristic) != null) {//todo skip houses for now
//                continue;
//            }
//            final Characteristic byString = Planet.getByString(characteristic);
//            if (byString != null) {
//                characteristic = byString.getString();
//            }
//
            Characteristic characteristic = planetInSign.getCharacteristic();
            if (planetInSign.getSigns().length > 1) {//todo - ?
                variations.append(characteristic).append("; ");
            } else {
                if (characteristic.equals(Planet.SUN) ||
                        characteristic.equals(Planet.MOON)) {
                    natalChartSpeech.append("The ");
                }
                natalChartSpeech.append(characteristic.getString());
                natalChartSpeech.append(" is in ");
                natalChartSpeech.append(planetInSign.toSignRange());
                natalChartSpeech.append("; ");
            }
        }
        if (variations.length() != 0) {
            natalChartSpeech.append("Please note, that signs of some planets, including ").append(variations).append(" may vary depending to the place and time of birth.");
        }
        return natalChartSpeech.toString();
    }

    protected String formatNoYear(String date) {
        try {
            return new SimpleDateFormat("dd MMMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (Exception e) {
            log.error("Date re-formatNoYear parse exception: " + date);
            return null;
        }
    }

    //input - dd MMMM
    private String formatDay(String dateStr) throws ParseException {
        final String day = dateStr.substring(0, 2);
        return day + suffixes[Integer.parseInt(day)] + dateStr.substring(3, dateStr.length());
    }

}
