package vs.productproducermanager.offer;

import vs.productproducermanager.offer.exception.OfferException;
import vs.productproducermanager.producer.ProductProducer;

import static java.lang.Thread.sleep;

public class OfferAgent implements Runnable {
    private ProductProducer productProducer;
    private int sleepMillis;

    public OfferAgent(ProductProducer productProducer, int sleepMillis) {
        this.productProducer = productProducer;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        while (true) {
            createOffers();
            try {
                sleep(sleepMillis);
            } catch (InterruptedException e) {
            }
        }
    }

    private void createOffers() {
        for(String productName : productProducer.getProducedProducts().keySet()) {
            try {
                productProducer.createOffer(productName);
            } catch (OfferException e) {
                System.err.println("WARNING : Create offer failed");
                e.printStackTrace();
            }
        }
    }
}
