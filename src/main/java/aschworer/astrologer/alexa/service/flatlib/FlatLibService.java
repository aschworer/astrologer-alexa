package aschworer.astrologer.alexa.service.flatlib;

import aschworer.astrologer.model.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */
public class FlatLibService {


    private static final Logger log = LoggerFactory.getLogger(FlatLibService.class);

    private FlatLibLambdaFunction flatlibLambdaFunction;

    public FlatLibService() {
        init();
    }

    private void init() {
        ResourceBundle config = ResourceBundle.getBundle("servicekeys");
        BasicAWSCredentials creds = new BasicAWSCredentials(config.getString("aws.accessKeyId"), config.getString("aws.secretKey"));
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion(Regions.US_EAST_1).build();
        flatlibLambdaFunction = LambdaInvokerFactory.builder().lambdaClient(lambda).build(FlatLibLambdaFunction.class);
    }

    public List<CharacteristicInSign> invoke(FlatLibBirthDetails flatLibBirthDetails) throws Exception {
        try {
            log.info("\n\n\n--------------------------------------Lambda request - flatLibBirthDetails: " + flatLibBirthDetails + "\n\n\n");
            Object response = flatlibLambdaFunction.getNatalChart(flatLibBirthDetails);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(Sign[].class, new SignsAdapter()).
                    create();
            String json = gson.toJson(response, ArrayList.class);
            Type listType = new TypeToken<ArrayList<CharacteristicInSign>>(){}.getType();
            List<CharacteristicInSign> natalChart = gson.fromJson(json, listType);
            log.info("For request - flatLibBirthDetails: " + flatLibBirthDetails);
            log.info("\n\n\n--------------------------------------Lambda result - natalChart: " + natalChart + "\n\n\n");
            return natalChart;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    interface FlatLibLambdaFunction {
        @LambdaFunction(functionName = "flatlib")
        Object getNatalChart(FlatLibBirthDetails request) throws Exception;
    }

}
