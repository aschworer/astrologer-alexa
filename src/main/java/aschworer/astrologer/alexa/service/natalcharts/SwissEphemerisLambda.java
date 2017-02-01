package aschworer.astrologer.alexa.service.natalcharts;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import aschworer.astrologer.alexa.service.NatalChartsServiceImpl;
import aschworer.astrologer.alexa.service.model.BirthDetails;
import aschworer.astrologer.alexa.service.model.NatalChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * @author aschworer
 */
public class SwissEphemerisLambda {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
    private static final Logger log = LoggerFactory.getLogger(NatalChartsServiceImpl.class);

    public NatalChart getNatalChart(BirthDetails birthDetails) throws Exception {
        AWSLambdaClient lambda = new AWSLambdaClient();
        lambda.configureRegion(Regions.US_EAST_1);
        NatalChartsLambda service = LambdaInvokerFactory.builder().lambdaClient(lambda).build(NatalChartsLambda.class);
        try {
            final NatalChart natalChart = new NatalChart(service.getNatalChart(birthDetails));
            log.info("result: " + natalChart);
            return natalChart;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw e;
        }
    }
}
