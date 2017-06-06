package vs.productproducermanager.producer;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import vs.productproducermanager.mqtt.MqttClientSingleton;
import vs.productproducermanager.offer.CreatedOffers;
import vs.productproducermanager.offer.Offer;
import vs.productproducermanager.offer.exception.OfferException;
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
                    "Offers", new MqttMessage(offer.toJSONString().getBytes()));
            System.out.println(String.format("INFO : New Offer created " +
                            "(offerID: %s, productProducerID: %s, productName: %s, productPricePerUnit: %s)",
                    offer.getOfferID(), offer.getProductName(), offer.getProductName(), offer.getProductPricePerUnit()));
        } catch (MqttException e) {
            throw new OfferException(e);
        }
    }
}
