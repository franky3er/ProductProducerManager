package vs.productproducermanager.request;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.offer.exception.OfferException;
import vs.productproducermanager.producer.ProductProducer;
import vs.productproducermanager.request.exception.RequestException;

import java.util.concurrent.BlockingQueue;

public class RequestAgent implements Runnable {
    private ProductProducer productProducer;
    private BlockingQueue<MqttMessage> requestTasks;

    public RequestAgent(ProductProducer productProducer, BlockingQueue<MqttMessage> requestTasks) {
        this.productProducer = productProducer;
        this.requestTasks = requestTasks;
    }

    @Override
    public void run() {
        System.out.println("INFO : Request Handler running");
        while (true) {
            try {
                handleRequestTask();
            } catch (RequestException e) {
                System.err.println("WARNING : Interpret Request failed");
                e.printStackTrace();
            } catch (OfferException e) {
                System.err.println("WARNING : Create Offer after receiving Request failed");
                e.printStackTrace();
            }
        }
    }

    private void handleRequestTask() throws RequestException, OfferException {
        Request request = receiveRequest();
        createOffer(request);
    }

    private Request receiveRequest() throws RequestException {
        try {
            MqttMessage requestTask = requestTasks.take();
            Request request = RequestFactory.create(requestTask.toString());
            System.out.println(String.format("INFO : Receive Request (productShopID: %s, productName: %s)",
                    request.getShopID(), request.getProductName()));
            return request;
        } catch (InterruptedException e) {
            throw new RequestException(e);
        } catch (ParseException e) {
            throw new RequestException(e);
        }
    }

    private void createOffer(Request request) throws OfferException {
        if (productProducer.getProducedProducts().containsKey(request.getProductName())) {
            productProducer.createOffer(request.getProductName());
        }
    }

}
