package aschworer.astrologer.alexa.service;

import aschworer.astrologer.alexa.service.model.Data;
import aschworer.astrologer.alexa.service.model.NatalChart;
import com.google.gson.Gson;
import aschworer.astrologer.alexa.service.model.CharacteristicInSign;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author aschworer
 */
public class MockAlexaNatalChartsService extends NatalChartsServiceImpl {

    public NatalChart getNatalChart(Date date, String lat, String lng) throws Exception {
        return new NatalChart(getPeopleCharts());
    }

    private List<CharacteristicInSign> getPeopleCharts() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("mockcharts.json");
        Gson gson = new Gson();
        final InputStreamReader reader = new InputStreamReader(in);
        Data data = gson.fromJson(reader, Data.class);
        return (data == null) ? new ArrayList<>() : data.getData();
    }
}
