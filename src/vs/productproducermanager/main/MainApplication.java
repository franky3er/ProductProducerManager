package vs.productproducermanager.main;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import vs.productproducermanager.mqtt.MqttClientSingleton;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainApplication {
    private final static String PROJECT_NAME = "ProductProducerManager";
    private final static String PROJECT_CONFIG = System.getProperty("user.dir") + File.separator +
            "config" + File.separator + PROJECT_NAME + ".properties";

    private final static String PRODUCTPRODUCERMANAGER_MQTT_IP = "ProductProducerManager.MQTT.IP";
    private final static String PRODUCTPRODUCERMANAGER_MQTT_PORT = "ProductProducerManager.MQTT.Port";

    private static String mqttIP;
    private static String mqttPort;

    private static MemoryPersistence persistence;
    private static MqttClientSingleton client;
    private static MqttConnectOptions connOpts;

    public static void main(String [] args) {
        try {
            loadConfig();
            initialize();
            run();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(PROJECT_CONFIG));
        mqttIP = properties.getProperty(PRODUCTPRODUCERMANAGER_MQTT_IP);
        mqttPort = properties.getProperty(PRODUCTPRODUCERMANAGER_MQTT_PORT);
    }

    private static void initialize() throws MqttException {
        initializeMqttClient();
    }

    private static void initializeMqttClient() throws MqttException {
        persistence = new MemoryPersistence();
        MqttClientSingleton.initialize(String.format("tcp://%s:%s", mqttIP, mqttPort), "TODO",
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
    }

    private static void run() {
    }
}
