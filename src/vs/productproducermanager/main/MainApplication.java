package vs.productproducermanager.main;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.mqtt.MqttClientSingleton;
import vs.productproducermanager.offer.OfferAgent;
import vs.productproducermanager.producer.ProductProducer;
import vs.productproducermanager.producer.ProductProducerFactory;
import vs.productproducermanager.request.RequestAgent;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainApplication {
    private final static String PROJECT_NAME = "ProductProducerManager";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTPRODUCERMANAGER_PRODUCTPRODUCER_CONFIGURATION_FILESOURCE =
            "ProductProducerManager.ProductProducer.Configuration.fileSource";
    private final static String PRODUCTPRODUCERMANAGER_MQTT_IP = "ProductProducerManager.MQTT.IP";
    private final static String PRODUCTPRODUCERMANAGER_MQTT_PORT = "ProductProducerManager.MQTT.Port";
    private final static String PRODUCTPRODUCERMANAGER_OFFERAGENT_SLEEPSECONDS =
            "ProductProducerManager.OfferAgent.SleepSeconds";
    private final static String PRODUCTPRODUCERMANAGER_REQUESTAGENT_QUANTITY = "ProductProducerManager.RequestAgent.Quantity";

    private static String productProducerConfigurationFileSource;
    private static String mqttIP;
    private static String mqttPort;
    private static long offerAgentSleepMillis;
    private static int requestAgentQuantity;

    private static MemoryPersistence persistence;
    private static MqttClientSingleton client;
    private static MqttConnectOptions connOpts;

    private static ProductProducer productProducer;

    private final static BlockingQueue<MqttMessage> requestTasks = new ArrayBlockingQueue<MqttMessage>(1024);

    private final static List<Thread> threads = new ArrayList<>();

    public static void main(String [] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (IOException e) {
            System.err.println("ERROR : IOException");
            e.printStackTrace();
        } catch (MqttException e) {
            System.err.println(String.format("ERROR : Connection to MQTTServer failed (IP: %s, Port: %s)",
                    mqttIP, mqttPort));
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("ERROR : Parsing failed");
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        System.out.println("INFO : Load Config");
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        productProducerConfigurationFileSource =
                properties.getProperty(PRODUCTPRODUCERMANAGER_PRODUCTPRODUCER_CONFIGURATION_FILESOURCE);
        mqttIP = properties.getProperty(PRODUCTPRODUCERMANAGER_MQTT_IP);
        mqttPort = properties.getProperty(PRODUCTPRODUCERMANAGER_MQTT_PORT);
        offerAgentSleepMillis = Long.parseLong(
                properties.getProperty(PRODUCTPRODUCERMANAGER_OFFERAGENT_SLEEPSECONDS)) * 1000;
        requestAgentQuantity = Integer.parseInt(
                properties.getProperty(PRODUCTPRODUCERMANAGER_REQUESTAGENT_QUANTITY));
    }

    private static void initialize() throws MqttException, IOException, ParseException {
        initializeProductProducer();
        initializeMqttClient();
        initializeOfferAgent();
        initializeRequestAgents();
    }

    private static void initializeProductProducer() throws IOException, ParseException {
        System.out.println(String.format("INFO : Create ProductProducer (FileSource: %s)",
                productProducerConfigurationFileSource));
        productProducer = ProductProducerFactory.create(productProducerConfigurationFileSource);
    }

    private static void initializeMqttClient() throws MqttException {
        persistence = new MemoryPersistence();
        MqttClientSingleton.initialize(String.format("tcp://%s:%s", mqttIP, mqttPort), "bla",
                persistence);
        client = MqttClientSingleton.getInstance();
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.err.println("Error : Lost connection to MQTT Server");
                throwable.getStackTrace();
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                switch(topic) {
                    case "Request": {
                        requestTasks.put(mqttMessage);
                    } break;
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });

        client.connect();
        System.out.println(String.format("INFO : Connected to MQTTServer (IP: %s, Port: %s)", mqttIP, mqttPort));
    }

    private static void initializeOfferAgent() {
        threads.add(new Thread(new OfferAgent(productProducer, offerAgentSleepMillis)));
    }

    private static void initializeRequestAgents() {
        for(int i = 0; i < requestAgentQuantity; i++) {
            threads.add(new Thread(new RequestAgent(productProducer, requestTasks)));
        }
    }

    private static void run() {
        for(Thread thread : threads) {
            thread.start();
        }
    }
}
