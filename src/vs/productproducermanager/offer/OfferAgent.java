package vs.productproducermanager.offer;

import vs.productproducermanager.offer.exception.OfferException;
import vs.productproducermanager.producer.ProductProducer;

import static java.lang.Thread.sleep;

public class OfferAgent implements Runnable {
    private ProductProducer productProducer;
    private long sleepMillis;

    public OfferAgent(ProductProducer productProducer, long sleepMillis) {
        this.productProducer = productProducer;
        this.sleepMillis = sleepMillis;
    }

    @Override
    public void run() {
        System.out.println("INFO : Offer Agent running");
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
