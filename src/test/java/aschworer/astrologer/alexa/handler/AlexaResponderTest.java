package aschworer.astrologer.alexa.handler;

import aschworer.astrologer.alexa.handler.responder.AlexaIntent;
import aschworer.astrologer.alexa.handler.responder.charts.*;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import aschworer.astrologer.alexa.handler.responder.charts.SpokenCards;

import static org.junit.Assert.assertEquals;

/**
 * @author aschworer
 */
@RunWith(MockitoJUnitRunner.class)
public class AlexaResponderTest {

    private AstrologerAlexaResponder astrologerAlexaResponder = new AstrologerAlexaResponder();

    @Mock
    private Session session;


    @Test
    public void testAMAZONStopIntent() throws Exception {
        assertEquals(SpokenCards.STOP, getResponseCardTitleForIntentName(AlexaIntent.AMAZON_STOP_INTENT.getName()));
    }

    @Test
    public void testAMAZONCancelIntent() throws Exception {
        assertEquals(SpokenCards.CANCEL, getResponseCardTitleForIntentName(AlexaIntent.AMAZON_CANCEL_INTENT.getName()));
    }

    @Test
    public void testAMAZONHelpIntent() throws Exception {
        assertEquals(SpokenCards.HELP, getResponseCardTitleForIntentName(AlexaIntent.AMAZON_HELP_INTENT.getName()));
    }

    private String getResponseCardTitleForIntentName(String intentName) throws SpeechletException {
        return astrologerAlexaResponder.respondToIntent(buildIntent(intentName), session).getCard().getTitle();
    }

    private Intent buildIntent(String intentName) {
        return Intent.builder().withName(intentName).build();
    }

}
