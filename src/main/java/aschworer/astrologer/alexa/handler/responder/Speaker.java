package aschworer.astrologer.alexa.handler.responder;

import aschworer.astrologer.alexa.handler.responder.charts.*;
import aschworer.astrologer.model.*;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;
import org.slf4j.*;

import java.text.*;
import java.time.format.*;
import java.util.*;

/**
 * @author aschworer
 */
public abstract class Speaker {
    //    public static final String LETS_TRY_AGAIN_PREFIX = "Hmm, not sure. Let's try that again. ";

    private static final Logger log = LoggerFactory.getLogger(Speaker.class);
    public static final String I_DIDN_T_CATCH_THAT_LET_S_TRY_AGAIN = "I didn't catch that. Let's try again. ";
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
        if (lastSaid == null) {
            return repeatedSpeech(SpokenCards.WELCOME);
        }
        return speak(cardName, I_DIDN_T_CATCH_THAT_LET_S_TRY_AGAIN + lastSaid, false);
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

    protected String getNatalChartAsSpeech(NatalChart natalChart, String missingInfoPhrase) {
        StringBuilder planetWithMultipleSigns = new StringBuilder();
        StringBuilder natalChartSpeech = new StringBuilder();
        for (CharacteristicInSign planetInSign : natalChart.getCharacteristicsInSigns()) {
            Characteristic characteristic = planetInSign.getCharacteristic();
            String cToString = characteristic.getString();
            if (planetInSign.getSigns().length > 1) {
                if (cToString != null && !(characteristic instanceof House)) planetWithMultipleSigns.append(cToString).append(", ");
            } else if (cToString != null) {
                if (characteristic.equals(Planet.SUN) || characteristic.equals(Planet.MOON)) natalChartSpeech.append("The ");
                natalChartSpeech.append(cToString).append(" is in ").append(planetInSign.toSignRange()).append(". ");
            }
        }

        if (planetWithMultipleSigns.length() == 2) {
            natalChartSpeech.append(planetWithMultipleSigns).append(" require ").append(missingInfoPhrase);
        } else if (planetWithMultipleSigns.length() > 2) {
            natalChartSpeech.append("Planets that are not mentioned require ").append(missingInfoPhrase)
                    .append(". Those include ").append(planetWithMultipleSigns)
                    .append(" Ascendant, Midheaven and the other houses.");
        }
        return natalChartSpeech.toString();
    }

    protected String formatNoYear(String date) throws AlexaDateTimeException {
        return DateTimeFormatter.ofPattern("dd MMMM").format(AlexaDateTimeUtil.parseDate(date));
    }

    protected String getMissingInfoPhrase(String place, String time) {
        String missingInfo = "";
        if (SessionDetails.UNKNOWN.equalsIgnoreCase(time)) missingInfo = missingInfo + " time";
        if (SessionDetails.UNKNOWN.equalsIgnoreCase(place)) {
            if (!missingInfo.isEmpty()) {
                missingInfo = missingInfo + " and ";
            }
            missingInfo = missingInfo + "place";
        }
        missingInfo = missingInfo + " of birth";
        return missingInfo;
    }

}
