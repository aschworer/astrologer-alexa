package aschworer.astrologer.alexa.handler;

import aschworer.astrologer.alexa.handler.responder.*;
import aschworer.astrologer.alexa.handler.responder.charts.*;
import aschworer.astrologer.model.*;
import com.amazon.speech.slu.*;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.*;
import org.hamcrest.*;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.mockito.runners.*;

import java.util.*;

import static aschworer.astrologer.alexa.handler.responder.charts.SessionDetails.*;
import static aschworer.astrologer.alexa.handler.responder.charts.AstrologerIntent.*;
import static aschworer.astrologer.alexa.handler.responder.charts.SpokenCards.*;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.*;

/**
 * @author aschworer
 */
@RunWith(MockitoJUnitRunner.class)
public class AstrologerResponderTest extends AlexaResponderTest {

    private AlexaResponder astrologerResponder = new AlexaResponder();

    @Mock
    private Session session;

    @Before
    public void init() {
        ResourceBundle config = ResourceBundle.getBundle("servicekeys");
        System.setProperty("aws.accessKeyId", config.getString("aws.accessKeyId"));
        System.setProperty("aws.secretKey", config.getString("aws.secretKey"));
    }

    @Test
    //no lambda involved
    public void testNatalChartIntent() throws Exception {
        final SpeechletResponse speechletResponse = astrologerResponder.respondToIntent(buildIntent(FULL_CHART_INTENT.getName()), session);
        assertEquals(TELL_ME_BIRTH_DAY, speechletResponse.getCard().getTitle());
        assertFalse(speechletResponse.getNullableShouldEndSession());
        Mockito.verify(session).setAttribute(INITIAL_INTENT, FULL_CHART_INTENT.getName());
    }

    @Test
    //no lambda involved
    public void testMoonSignIntent() throws Exception {
        final SpeechletResponse speechletResponse = astrologerResponder.respondToIntent(buildIntentWithSlots(PLANET_SIGN_INTENT.getName(), buildSlotsMap("planet", "Moon")), session);
        assertEquals(TELL_ME_BIRTH_DAY, speechletResponse.getCard().getTitle());
        assertFalse(speechletResponse.getNullableShouldEndSession());
        Mockito.verify(session).setAttribute(INITIAL_INTENT, PLANET_SIGN_INTENT.getName());
    }

    @Test
    //no lambda involved
    public void testSunSignIntent() throws Exception {
        final SpeechletResponse speechletResponse = astrologerResponder.respondToIntent(buildIntent(SUN_SIGN_INTENT.getName()), session);
        assertEquals(TELL_ME_BIRTH_DAY, speechletResponse.getCard().getTitle());
        assertFalse(speechletResponse.getNullableShouldEndSession());
        Mockito.verify(session).setAttribute(INITIAL_INTENT, SUN_SIGN_INTENT.getName());
    }

    @Test
    //no lambda involved
    public void testBirthDateIntent() throws Exception {
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1985-11-20");
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(SUN_SIGN_INTENT.getName());
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_DAY_INTENT.getName(), buildSlotsMap("day", "1985-11-20")), session);
        Mockito.verify(session).setAttribute(BIRTH_DATE, "1985-11-20");
        assertEquals(DOUBLE_CHECK_DATE, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    public void testBirthDateOnSunSignIntentYearMissing() throws Exception {
        String date = AstrologerResponder.CURRENT_YEAR + "-11-20";
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn(date);
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(SUN_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(PLANET)).thenReturn(Planet.SUN.getString());
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_DAY_INTENT.getName(), buildSlotsMap("day", date)), session);
        Mockito.verify(session).setAttribute(BIRTH_DATE, date);
        assertEquals(DOUBLE_CHECK_DATE, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    //no lambda involved
    public void testBirthDateIntentYearMissing() throws Exception {
        String date = AstrologerResponder.CURRENT_YEAR + "-11-20";
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn(date);
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_DAY_INTENT.getName(), buildSlotsMap("day", date)), session);
        Mockito.verify(session).setAttribute(BIRTH_DATE, date);
        assertEquals(TELL_ME_BIRTH_YEAR, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    //no lambda involved
    public void testDenyDateIntent() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(LAST_TELLME_CARD)).thenReturn(TELL_ME_BIRTH_DAY);
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(NO_INTENT.getName()).build(), session);
        assertEquals(TELL_ME_BIRTH_DAY, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    //no lambda involved
    public void testBirthYearIntent() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("2015-11-20");
        Mockito.when(session.getAttribute(LAST_TELLME_CARD)).thenReturn(TELL_ME_BIRTH_YEAR);
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_YEAR_OR_TIME_INTENT.getName(), buildSlotsMap("year", "1986")), session);
//        Mockito.verify(session).setAttribute(BIRTH_YEAR, "1986");
        assertEquals(DOUBLE_CHECK_DATE, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    //lambda involved
    public void testConfirmBirthDateIntent_OnSunSign_Success() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(SUN_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1985-11-29");
        Mockito.when(session.getAttribute(PLANET)).thenReturn(Planet.SUN.getString());
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName()).build(), session);
        assertEquals(SPEAK_PLANET_SIGN, response.getCard().getTitle());
        assertTrue(response.getNullableShouldEndSession());
        assertResponseMentionSigns(response);
        assertTrue(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains("29"));
        assertTrue(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains("1985"));
    }

    @Test
    //lambda involved
    public void testConfirmBirthDateIntent_OnSunSign_NoYear() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(SUN_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn(AstrologerResponder.CURRENT_YEAR + "-11-29");
        Mockito.when(session.getAttribute(PLANET)).thenReturn(Planet.SUN.getString());
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName()).build(), session);
        assertEquals(SPEAK_PLANET_SIGN, response.getCard().getTitle());
        assertTrue(response.getNullableShouldEndSession());
        assertResponseMentionSigns(response);
        assertTrue(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains("29"));
        assertFalse(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains(AstrologerResponder.CURRENT_YEAR));
    }

    @Test
    //lambda involved
    public void testChartSuccess() throws Exception {
        final String place = "Boston";
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(FULL_CHART_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1986-04-20");
        Mockito.when(session.getAttribute(BIRTH_TIME)).thenReturn("20:56");
        Mockito.when(session.getAttribute(BIRTH_PLACE)).thenReturn(place);
        Mockito.when(session.getAttribute(BIRTH_LAT)).thenReturn("+33.33");
        Mockito.when(session.getAttribute(BIRTH_LNG)).thenReturn("-33.33");
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName()).build(), session);
        assertEquals(SPEAK_NATAL_CHART, response.getCard().getTitle());
        assertTrue(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains("house "));
        assertTrue(response.getNullableShouldEndSession());
        assertResponseMentions(response, "in " + place);
        assertResponseMentions(response, "1986-04-20");
        assertResponseMentionSigns(response);
    }

    @Test
    public void testChartDoubleCheckPlace() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(FULL_CHART_INTENT.getName());
        final String country = "Australia";
        Mockito.when(session.getAttribute(BIRTH_PLACE)).thenReturn(country);
        Mockito.when(session.getAttribute(LAST_TELLME_CARD)).thenReturn(TELL_ME_BIRTH_PLACE);
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_PLACE_INTENT.getName(),
                buildSlotsMap(BIRTH_PLACE, country)), session);
        assertEquals(DOUBLE_CHECK_PLACE, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    public void testChartBirthTimeRequired() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(FULL_CHART_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1986-04-20");
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName())
                .build(), session);
        assertEquals(TELL_ME_BIRTH_TIME, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    public void testMoonSignMultipleSignsSoBirthTimeRequired() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1986-04-20");
        Mockito.when(session.getAttribute(BIRTH_PLACE)).thenReturn("some place");
        Mockito.when(session.getAttribute(BIRTH_LAT)).thenReturn("+33.33");
        Mockito.when(session.getAttribute(BIRTH_LNG)).thenReturn("-33.33");
        Mockito.when(session.getAttribute(PLANET)).thenReturn("MOON");
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName()).build(), session);
        assertEquals(TELL_ME_BIRTH_TIME, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    public void testMoonSignSuccess() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1986-04-20");
        Mockito.when(session.getAttribute(BIRTH_TIME)).thenReturn("21:20");
        Mockito.when(session.getAttribute(BIRTH_PLACE)).thenReturn("some place");
        Mockito.when(session.getAttribute(BIRTH_LAT)).thenReturn("+33.33");
        Mockito.when(session.getAttribute(BIRTH_LNG)).thenReturn("-33.33");
        Mockito.when(session.getAttribute(PLANET)).thenReturn("MOON");
        final SpeechletResponse response = astrologerResponder.respondToIntent(Intent.builder().withName(YES_INTENT.getName()).build(), session);
        assertEquals(SPEAK_PLANET_SIGN, response.getCard().getTitle());
        assertTrue(response.getNullableShouldEndSession());
        assertResponseMentionSigns(response);
    }

    @Test
    public void testMoonSignTimeThatLooksLikeYear() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(PLANET_SIGN_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1986-04-20");
        Mockito.when(session.getAttribute(BIRTH_TIME)).thenReturn("11:20");
        Mockito.when(session.getAttribute(BIRTH_DATE_CONFIRMED)).thenReturn(Boolean.TRUE);
        Mockito.when(session.getAttribute(PLANET)).thenReturn("MOON");
        Mockito.when(session.getAttribute(LAST_TELLME_CARD)).thenReturn(TELL_ME_BIRTH_TIME);
//        Mockito.when(session.getAttribute(LAST_TELLME_SPEECH)).thenReturn("blah");
        final SpeechletResponse response = astrologerResponder.respondToIntent(buildIntentWithSlots(BIRTH_YEAR_OR_TIME_INTENT.getName(),
                buildSlotsMap("year", "1120")), session);
//        Mockito.verify(session).setAttribute(BIRTH_DATE_CONFIRMED, Boolean.TRUE);
//        Mockito.verify(session).setAttribute(BIRTH_TIME, "11:20");
        assertEquals(TELL_ME_BIRTH_PLACE, response.getCard().getTitle());
        assertFalse(response.getNullableShouldEndSession());
    }

    @Test
    public void testChartDoubleCheckDate() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(FULL_CHART_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1985-11-20");
        assertEquals(DOUBLE_CHECK_DATE, astrologerResponder.respondToIntent(
                buildIntentWithSlots(BIRTH_DAY_INTENT.toString(), buildSlotsMap("day", "1985-11-20")),
                session).getCard().getTitle());
    }

    @Test
    public void testBirthTimeIntentOnNatalChartBirthPlaceRequired() throws Exception {
        Mockito.when(session.getAttribute(INITIAL_INTENT)).thenReturn(FULL_CHART_INTENT.getName());
        Mockito.when(session.getAttribute(BIRTH_DATE)).thenReturn("1985-11-20");
        Mockito.when(session.getAttribute(BIRTH_TIME)).thenReturn("21:20");
        Mockito.when(session.getAttribute(LAST_TELLME_CARD)).thenReturn(TELL_ME_BIRTH_PLACE);
        assertEquals(DOUBLE_CHECK_PLACE, astrologerResponder.respondToIntent(
                Intent.builder().withName(BIRTH_PLACE_INTENT.toString()).withSlots(
                        buildSlotsMap("place", "moscow")).build(), session).getCard().getTitle());
    }


    private HashMap<String, Slot> buildSlotsMap(String name, String value) {
        HashMap<String, Slot> slots = new HashMap<>();
        slots.put(name, Slot.builder()
                .withName(name)
                .withValue(value).build());
        return slots;
    }

    private Intent buildIntentWithSlots(String intentName, HashMap<String, Slot> slots) {
        return Intent.builder().withName(intentName).withSlots(slots).build();
    }

    private void assertResponseMentionSigns(SpeechletResponse response) {
        assertThat(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase(), CoreMatchers.anyOf(
                containsString(Sign.AQUARIUS.toString().toLowerCase()),
                containsString(Sign.ARIES.toString().toLowerCase()),
                containsString(Sign.CANCER.toString().toLowerCase()),
                containsString(Sign.CAPRICORN.toString().toLowerCase()),
                containsString(Sign.GEMINI.toString().toLowerCase()),
                containsString(Sign.LEO.toString().toLowerCase()),
                containsString(Sign.LIBRA.toString().toLowerCase()),
                containsString(Sign.PISCES.toString().toLowerCase()),
                containsString(Sign.SAGITTARIUS.toString().toLowerCase()),
                containsString(Sign.SCORPIO.toString().toLowerCase()),
                containsString(Sign.TAURUS.toString().toLowerCase()),
                containsString(Sign.VIRGO.toString().toLowerCase())
//                containsString(Sign.VARY.toString().toLowerCase())
        ));
    }

    private void assertResponseMentions(SpeechletResponse response, String string) {
        assertTrue(((SsmlOutputSpeech) response.getOutputSpeech()).getSsml().toLowerCase().contains(string.toLowerCase()));
    }

    private Intent buildIntent(String intentName) {
        return Intent.builder().withName(intentName).build();
    }

}
