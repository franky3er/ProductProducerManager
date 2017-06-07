package vs.productproducermanager.delivery;

import org.json.simple.JSONObject;
import vs.productproducermanager.offer.Offer;

public class Delivery {
    private Offer offer;
    private double productAmount;
    private String shopID;

    public Delivery(Offer offer, double productAmount, String shopID) {
        this.offer = offer;
        this.productAmount = productAmount;
        this.shopID = shopID;
    }

    public JSONObject toJSONObject() {
        JSONObject deliveryJSONObject = new JSONObject();
        deliveryJSONObject.put("offer", offer.toJSONObject());
        deliveryJSONObject.put("productAmount", productAmount);
        deliveryJSONObject.put("shopID", shopID);
        return deliveryJSONObject;
    }

    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }

    public Offer getOffer() {
        return offer;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public String getShopID() {
        return shopID;
    }
}
