package vs.productproducermanager.offer;

import org.json.simple.JSONObject;

public class Offer {
    private int offerID;
    private String producerID;
    private String productName;
    private long productPricePerUnit;

    public Offer(int offerID, String producerID, String productName, long productPricePerUnit) {
        this.offerID = offerID;
        this.producerID = producerID;
        this.productName = productName;
        this.productPricePerUnit = productPricePerUnit;
    }

    public JSONObject toJSONObject() {
        JSONObject offerJSONObject = new JSONObject();
        offerJSONObject.put("offerID", offerID);
        offerJSONObject.put("producerID", producerID);
        offerJSONObject.put("productName", productName);
        offerJSONObject.put("productPricePerUnit", productPricePerUnit);
        return offerJSONObject;
    }

    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }

    public int getOfferID() {
        return offerID;
    }

    public String getProducerID() {
        return producerID;
    }

    public String getProductName() {
        return productName;
    }

    public long getProductPricePerUnit() {
        return productPricePerUnit;
    }
}
