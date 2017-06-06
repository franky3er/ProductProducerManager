package vs.productproducermanager.main;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.mqtt.MqttClientSingleton;
import vs.productproducermanager.producer.ProductProducer;
import vs.productproducermanager.producer.ProductProducerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainApplication {
    private final static String PROJECT_NAME = "ProductProducerManager";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTPRODUCERMANAGER_PRODUCTPRODUCER_CONFIGURATION_FILESOURCE =
            "ProductProducerManager.ProductProducer.Configuration.fileSource";
    private final static String PRODUCTPRODUCERMANAGER_MQTT_IP = "ProductProducerManager.MQTT.IP";
    private final static String PRODUCTPRODUCERMANAGER_MQTT_PORT = "ProductProducerManager.MQTT.Port";

    private static String productProducerConfigurationFileSource;
    private static String mqttIP;
    private static String mqttPort;

    private static MemoryPersistence persistence;
    private static MqttClientSingleton client;
    private static MqttConnectOptions connOpts;

    private static ProductProducer productProducer;

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
    }

    private static void initialize() throws MqttException, IOException, ParseException {
        initializeProductProducer();
        initializeMqttClient();
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
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //TODO implement message arrived
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });

        client.connect();
        System.out.println(String.format("INFO : Connected to MQTTServer (IP: %s, Port: %s)", mqttIP, mqttPort));
    }

    private static void run() {
    }
}
