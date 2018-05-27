package aschworer.astrologer.alexa.service.repository;

import aschworer.astrologer.model.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

/**
 * @author aschworer
 */

/**
 * For extended version only - do not use before
 */
public class AstrologerRepository /*implements NatalChartsService*/ {

    public static final DateTimeFormatter ASTROLOGER_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static final DateTimeFormatter ASTROLOGER_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);

    private static final Logger log = LoggerFactory.getLogger(AstrologerRepository.class);

    private AstrologerLambdaFunction astrologerLambdaFunction;

//    private GeocodeLocationService locationService = new GeocodeLocationService();
//    private AstrologerLambdaService swissEphLambdaFunction = new AstrologerLambdaService();

    public AstrologerRepository() {
        init();
    }

    //todo remove
//    @Override
    public Sign[] getPlanetSign(Planet planet, LocalDate date, String lat, String lng) throws Exception {
        return getNatalChart(date, lat, lng).getSign(planet);
    }

    //todo remove
    public NatalChart getNatalChart(LocalDate date, String lat, String lng) throws Exception {
        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");//todo new charts only via flatlib
        Person person = new Person();
        person.setBirthPlace(lat + "," + lng);//todo
        person.setDob(date);
        request.setPerson(person);
        AstrologerLambdaResponse response = invoke(request);
        return response.getNatalChart();
    }

    private void init() {
        ResourceBundle config = ResourceBundle.getBundle("servicekeys");
        BasicAWSCredentials creds = new BasicAWSCredentials(config.getString("aws.accessKeyId"), config.getString("aws.secretKey"));
        AWSLambda lambda = AWSLambdaClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(creds)).withRegion("us-east-1").build();
        astrologerLambdaFunction = LambdaInvokerFactory.builder().lambdaClient(lambda).build(AstrologerLambdaFunction.class);
    }

    protected AstrologerLambdaResponse invoke(AstrologerLambdaRequest astrologerLambdaRequest) throws Exception {
        try {
            log.info("\n\n\n--------------------------------------Lambda request : " + astrologerLambdaRequest + "\n\n\n");
            Object response = astrologerLambdaFunction.invoke(astrologerLambdaRequest);
            Gson gson = new GsonBuilder().
                    registerTypeAdapter(Sign[].class, new SignsAdapter()).
                    create();
            String json = gson.toJson(response, LinkedHashMap.class);
            AstrologerLambdaResponse astrologerLambdaResponse = gson.fromJson(json, AstrologerLambdaResponse.class);
            log.info("For request - : " + astrologerLambdaRequest);
            log.info("\n\n\n--------------------------------------Lambda result: " + response + "\n\n\n");
            return astrologerLambdaResponse;
        } catch (Exception e) {
            log.error("error: " + e.getMessage());
            throw e;
        }
    }

    interface AstrologerLambdaFunction {
        @LambdaFunction(functionName = "repository")
        Object invoke(AstrologerLambdaRequest request) throws Exception;
    }


    //    public static void main(String[] args) throws Exception {
//        AstrologerLambdaRequest request = new AstrologerLambdaRequest("getPeopleByDominantElement");
//        request.setElement(Element.AIR);
//        AstrologerLambdaResponse response = new AstrologerLambdaService().invoke(request);
//        System.out.println(response);

//        AstrologerLambdaRequest request = new AstrologerLambdaRequest("loadNewChart");
//        Person person = new Person();
//        person.setBirthPlace("Nadym");
//        person.setDob("20/11/1985");
//        request.setPerson(person);
//        AstrologerLambdaResponse response = new AstrologerLambdaService().invoke(request);
//        System.out.println(getChartDisplay(response.getNatalChart(), new Characteristic[]{}));
//    }


    /*private static String getChartDisplay(NatalChart chart, Characteristic[] whatToDisplay) {
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
    }*/

}