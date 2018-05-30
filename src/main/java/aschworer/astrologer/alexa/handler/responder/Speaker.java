package aschworer.astrologer.alexa.handler.responder;

import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author aschworer
 */
public abstract class Speaker {
//    public static final String LETS_TRY_AGAIN_PREFIX = "Hmm, not sure. Let's try that again. ";

    private static final Logger log = LoggerFactory.getLogger(Speaker.class);
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

    protected SpeechletResponse repeatedSpeech(String cardName) {
        return speak(cardName, messages.getString(cardName), false);
    }

    protected SpeechletResponse speakAndFinish(String cardName, String... args) {
        return speak(cardName, MessageFormat.format(messages.getString(cardName), args), true);
    }

    protected SpeechletResponse repeat(String cardName, String lastSaid) {
        return speak(cardName, lastSaid, false);
    }

    private SpeechletResponse speak(String cardName, String speechText, boolean end) {
        SimpleCard card = new SimpleCard();
        card.setTitle(cardName);
        card.setContent(speechText);
        SsmlOutputSpeech speech = new SsmlOutputSpeech();
        speech.setSsml("<speak>" + speechText + "</speak>");
//        log.info("return speech " + speechText);
        if (end) {
            return SpeechletResponse.newTellResponse(speech, card);
        } else {
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(speech);
            return SpeechletResponse.newAskResponse(speech, reprompt, card);
        }
    }

    protected String getNatalChartAsString(NatalChart natalChart) {
        StringBuilder planetWithMultipleSigns = new StringBuilder();
        StringBuilder natalChartSpeech = new StringBuilder();
        for (CharacteristicInSign planetInSign : natalChart.getCharacteristicsInSigns()) {
            Characteristic characteristic = planetInSign.getCharacteristic();
            String cToString = characteristic.getString();
            if (planetInSign.getSigns().length > 1) {
                if (!(characteristic instanceof House)) planetWithMultipleSigns.append(cToString).append(", ");
            } else if (cToString != null) {
                if (characteristic.equals(Planet.SUN) ||
                        characteristic.equals(Planet.MOON)) {
                    natalChartSpeech.append("The ");
                }
                natalChartSpeech.append(cToString);
                natalChartSpeech.append(" is in ");
                natalChartSpeech.append(planetInSign.toSignRange());
                natalChartSpeech.append(". ");
            }
        }
        if (planetWithMultipleSigns.length() > 0) {
            natalChartSpeech.append(" Planets that are not mentioned might require more information, like the time or place of birth. ")
                    .append("Those include ")
                    .append(planetWithMultipleSigns);
//                    .append(", Ascendant, Midheaven and other Houses");
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
