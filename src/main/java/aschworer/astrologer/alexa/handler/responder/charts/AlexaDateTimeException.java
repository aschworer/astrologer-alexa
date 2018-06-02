package aschworer.astrologer.alexa.handler.responder.charts;

public class AlexaDateTimeException extends Exception {

    private String spokenCard;

    AlexaDateTimeException(String spokenCard) {
        this.spokenCard = spokenCard;
    }

    String getSpokenCard() {
        return spokenCard;
    }
}
