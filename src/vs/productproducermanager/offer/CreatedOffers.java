package vs.productproducermanager.offer;

import java.util.HashMap;
import java.util.Map;

public class CreatedOffers {
    private int idCount;
    private Map<Integer, Offer> offers;

    public CreatedOffers() {
        idCount = 1;
        offers = new HashMap<>();
    }

    public synchronized Offer createOfferAndAdd(String producerID, String productName, long productPricePerUnit) {
        Offer offer = new Offer(idCount, producerID, productName, productPricePerUnit);
        offers.put(idCount, offer);
        idCount++;
        return offer;
    }

    public synchronized boolean existsOffer(int offerID) {
        return offers.containsKey(offerID);
    }

    public synchronized Offer getOffer(int offerID) {
        return offers.get(offerID);
    }

    public synchronized Offer removeOffer(int offerID) {
        return offers.remove(offerID);
    }
}
