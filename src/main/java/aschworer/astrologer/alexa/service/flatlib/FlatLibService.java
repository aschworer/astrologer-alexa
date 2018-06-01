package aschworer.astrologer.alexa.service.flatlib;

import aschworer.astrologer.model.*;
import com.amazonaws.auth.*;
import com.amazonaws.regions.*;
import com.amazonaws.services.lambda.*;
import com.amazonaws.services.lambda.invoke.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

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
            log.info("\n\n------Lambda request - flatLibBirthDetails: " + flatLibBirthDetails + "\n\n");
            long startTime = System.currentTimeMillis();
            Object response = flatlibLambdaFunction.getNatalChart(flatLibBirthDetails);
//            List<CharacteristicInSign> natalChart = parseWithJackson(response);
            List<CharacteristicInSign> natalChart = parseWithGson(response);
            long endTime = System.currentTimeMillis();
            log.info("\n\n------Lambda result (took " + (endTime - startTime) + " ms): " + natalChart + "\n\n");
            return natalChart;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    private List<CharacteristicInSign> parseWithGson(Object response) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Sign[].class, new SignsAdapter()).create();
        String json = gson.toJson(response, ArrayList.class);
        Type type = new TypeToken<ArrayList<CharacteristicInSign>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private List<CharacteristicInSign> parseWithJackson(Object response) {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Sign[].class, new SignsAdapter());
        module.addDeserializer(Sign[].class, new SignsAdapter.SignsDeserializer());
        mapper.registerModule(module);
        return mapper.convertValue(response, new TypeReference<List<CharacteristicInSign>>() {
        });
    }

    interface FlatLibLambdaFunction {
        @LambdaFunction(functionName = "flatlib")
        Object getNatalChart(FlatLibBirthDetails request) throws Exception;
    }

}
