package vs.productproducermanager.order;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.delivery.exception.DeliveryException;
import vs.productproducermanager.order.exception.OrderException;
import vs.productproducermanager.producer.ProductProducer;

import java.util.concurrent.BlockingQueue;

public class OrderAgent implements Runnable {
    private ProductProducer productProducer;
    private BlockingQueue<MqttMessage> orderTasks;

    public OrderAgent(ProductProducer productProducer, BlockingQueue<MqttMessage> orderTasks) {
        this.productProducer = productProducer;
        this.orderTasks = orderTasks;
    }

    @Override
    public void run() {
        System.out.println("INFO : Order Agent running");
        while(true) {
            try {
                handleOrderTask();
            } catch (OrderException e) {
                System.err.println("WARNING : Interpret Order failed");
                e.printStackTrace();
            } catch (DeliveryException e) {
                System.err.println("WARNING : Delivery failed");
                e.printStackTrace();
            }
        }
    }

    private void handleOrderTask() throws OrderException, DeliveryException {
        Order order = receiveOrder();
        productProducer.handleOrder(order);
    }

    private Order receiveOrder() throws OrderException {
        try {
            MqttMessage orderTask = orderTasks.take();
            Order order = OrderFactory.create(orderTask.toString());
            System.out.println(String.format("INFO : Receive Order " +
                    "(productShopID: %s, offerID: %s, productName: %s, productAmount: %s)",
                    order.getShopID(), order.getOffer().getOfferID(),
                    order.getOffer().getProductName(), order.getProductAmount()));
            return order;
        } catch (InterruptedException e) {
            throw new OrderException(e);
        } catch (ParseException e) {
            throw new OrderException(e);
        }
    }
}
