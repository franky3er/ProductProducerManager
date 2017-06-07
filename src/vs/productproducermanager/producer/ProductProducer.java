package vs.productproducermanager.producer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import vs.productproducermanager.delivery.Delivery;
import vs.productproducermanager.delivery.exception.DeliveryException;
import vs.productproducermanager.mqtt.MqttClientSingleton;
import vs.productproducermanager.offer.CreatedOffers;
import vs.productproducermanager.offer.Offer;
import vs.productproducermanager.offer.exception.OfferException;
import vs.productproducermanager.order.Order;
import vs.productproducermanager.product.ProducedProduct;

import java.util.Map;

public class ProductProducer {
    private String productProducerID;
    private Map<String, ProducedProduct> producedProducts;
    private CreatedOffers createdOffers;

    public ProductProducer(String productProducerID, Map<String, ProducedProduct> producedProducts) {
        this.productProducerID = productProducerID;
        this.producedProducts = producedProducts;
        this.createdOffers = new CreatedOffers();
    }

    public String getProductProducerID() {
        return productProducerID;
    }

    public Map<String, ProducedProduct> getProducedProducts() {
        return producedProducts;
    }

    public void createOffer(String productName) throws OfferException {
        try {
            long productPricePerUnit = producedProducts.get(productName).createProductPricePerUnit();
            Offer offer = createdOffers.createOfferAndAdd(productProducerID, productName, productPricePerUnit);
            MqttClientSingleton.getInstance().publish(
                    "Offer", new MqttMessage(offer.toJSONString().getBytes()));
            System.out.println(String.format("INFO : New Offer created " +
                            "(offerID: %s, productProducerID: %s, productName: %s, productPricePerUnit: %s)",
                    offer.getOfferID(), offer.getProductName(), offer.getProductName(), offer.getProductPricePerUnit()));
        } catch (MqttException e) {
            throw new OfferException(e);
        }
    }

    public void handleOrder(Order order) throws DeliveryException {
        if(order.getOffer().getProducerID().equals(productProducerID)) {
            Offer offer = createdOffers.removeOffer(order.getOffer().getOfferID());
            if(offer != null) {
                deliver(order);
            }
        }
    }

    private void deliver(Order order) throws DeliveryException {
        try {
            Delivery delivery = new Delivery(order.getOffer(), order.getProductAmount(), order.getShopID());
            MqttClientSingleton.getInstance().publish("Delivery",
                    new MqttMessage(delivery.toJSONString().getBytes()));
            System.out.println(String.format("INFO : New Delivery " +
                    "(productShopID: %s, productOfferID: %s productName: %s, productAmount: %s)",
                    delivery.getShopID(), delivery.getOffer().getOfferID(),
                    delivery.getOffer().getProductName(), delivery.getProductAmount()));
        } catch (MqttException e) {
            throw new DeliveryException(e);
        }
    }
}
