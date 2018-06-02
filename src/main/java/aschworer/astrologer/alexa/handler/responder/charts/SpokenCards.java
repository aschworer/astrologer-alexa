package aschworer.astrologer.alexa.handler.responder.charts;

/**
 * @author aschworer
 */
public interface SpokenCards {

    String WELCOME = "Welcome";
    String CANCEL = "Cancel";
    String HELP = "Help";
    String STOP = "Stop";

    String TELL_ME_BIRTH_DAY = "TellMeBirthday";
    String TELL_ME_BIRTH_TIME = "TellMeTimeOfBirth";
    String WHATS_BIRTH_YEAR = "WhatsBirthYear";
    String TELL_ME_BIRTH_PLACE = "TellMePlaceOfBirth";

    String DOUBLE_CHECK_DATE = "DoubleCheckDate";
    String DOUBLE_CHECK_PLACE = "DoubleCheckPlace";

    String SPEAK_PLANET_SIGN = "PlanetSign";
    String SPEAK_NATAL_CHART = "NatalChart";

    String MORE_DATA_REQUIRED = "MoreDataRequired";
    String CHART_ERROR = "NatalChartError";
    String INVALID_DATE = "InvalidDate";
    String INVALID_DATE_RANGE = "InvalidDateRange";
    String INVALID_TIME = "InvalidTime";
}
