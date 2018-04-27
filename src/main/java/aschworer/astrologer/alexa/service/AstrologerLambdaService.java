package aschworer.astrologer.alexa.service;

import aschworer.astrologer.model.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;


public class AstrologerLambdaService {

    public static final DateTimeFormatter ASTROLOGER_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter ASTROLOGER_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static final Logger log = LoggerFactory.getLogger(AstrologerLambdaService.class);

    private AstrologerLambdaFunction astrologerLambdaFunction;

    public AstrologerLambdaService() {
        init();
    }


    public static void main(String[] args) throws Exception {
//        AstrologerLambdaRequest request = new AstrologerLambdaRequest("getPeopleByDominantElement");
//        request.setElement(Element.AIR);
//        AstrologerLambdaResponse response = new AstrologerLambdaService().invoke(request);
//        System.out.println(response);

        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");
        Person person = new Person();
//        person.setBirthPlace("Nadym");
        person.setDob("20/11/1985");
        request.setPerson(person);
        AstrologerLambdaResponse response = new AstrologerLambdaService().invoke(request);
        System.out.println(getChartDisplay(response.getNatalChart(), new Characteristic[]{}));
    }

    private void init() {
        ResourceBundle config = ResourceBundle.getBundle("lambda");
        BasicAWSCredentials creds = new BasicAWSCredentials(config.getString("aws.accessKeyId"), config.getString("aws.secretKey"));
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-1").build();
        astrologerLambdaFunction = LambdaInvokerFactory.builder().lambdaClient(lambda).build(AstrologerLambdaFunction.class);
    }

    protected AstrologerLambdaResponse invoke(AstrologerLambdaRequest astrologerLambdaRequest) throws Exception {
        try {
            log.info("\n\n\n--------------------------------------Lambda request : " + astrologerLambdaRequest + "\n\n\n");
            Object response = astrologerLambdaFunction.invoke(astrologerLambdaRequest);
            Gson gson = new GsonBuilder().registerTypeAdapter(Characteristic.class, new InterfaceAdapter<Characteristic>()).
                    registerTypeAdapter(Sign[].class, new SignDeserializer()).
                    create();
            String json = gson.toJson(response, LinkedHashMap.class);
            AstrologerLambdaResponse astrologerLambdaResponse = gson.fromJson(json, AstrologerLambdaResponse.class);
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//            AstrologerLambdaResponse astrologerLambdaResponse = objectMapper.readValue(json, AstrologerLambdaResponse.class);
            log.info("For request - : " + astrologerLambdaRequest);
            log.info("\n\n\n--------------------------------------Lambda result: " + response + "\n\n\n");
            return astrologerLambdaResponse;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    interface AstrologerLambdaFunction {
        @LambdaFunction(functionName = "astrologer")
        Object invoke(AstrologerLambdaRequest request) throws Exception;
    }


    private static String getChartDisplay(NatalChart chart, Characteristic[] whatToDisplay) {
        StringBuilder listString = new StringBuilder();
        List<Characteristic> whatToPrintList = Arrays.asList(whatToDisplay);
        listString.append("\n");
        listString.append("Name: ").append(chart.getPerson().getName());
        listString.append("\n");
        listString.append("Born: ").append(chart.getPerson().getDob()).append((chart.getPerson().getTob() != null) ? ", " + chart.getPerson().getTob() : "").append("; ").append("\n").
                append(chart.getPerson().getBirthPlace());
        listString.append("\n-----------------------------------------\n");
        List<CharacteristicInSign> characteristicsInSigns = chart.getCharacteristicsInSigns();
        for (CharacteristicInSign s : characteristicsInSigns) {
            if (!whatToPrintList.isEmpty() && !whatToPrintList.contains(s.getCharacteristic())) {
                continue;
            }
            listString.append(s).append("\n");
        }
        return listString.toString();
    }
}

