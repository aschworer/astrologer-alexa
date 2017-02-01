package aschworer.astrologer.alexa.service.natalcharts;

import com.amazonaws.services.lambda.invoke.LambdaFunction;
import aschworer.astrologer.alexa.service.model.BirthDetails;
import aschworer.astrologer.alexa.service.model.CharacteristicInSign;

import java.util.List;

/**
 * @author aschworer
 */
public interface NatalChartsLambda {

    @LambdaFunction(functionName = "flatlib")
    List<CharacteristicInSign> getNatalChart(BirthDetails request) throws Exception;
}
