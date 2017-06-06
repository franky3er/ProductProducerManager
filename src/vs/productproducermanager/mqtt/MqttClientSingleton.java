package vs.productproducermanager.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttClientSingleton extends org.eclipse.paho.client.mqttv3.MqttClient {
    private static MqttClientSingleton mqttClient;

    private MqttClientSingleton(String serverURI, String clientId) throws MqttException {
        super(serverURI, clientId);
    }

    private MqttClientSingleton(String serverURI, String clientId, MemoryPersistence persistence) throws MqttException {
        super(serverURI, clientId, persistence);
    }

    public static void initialize(String serverURI, String clientId, MemoryPersistence persistence) throws MqttException {
        mqttClient = new MqttClientSingleton(serverURI, clientId, persistence);
    }

    public static MqttClientSingleton getInstance() {
        return mqttClient;
    }
}
