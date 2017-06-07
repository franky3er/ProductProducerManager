package vs.productproducermanager.order;

import org.json.simple.JSONObject;
import vs.productproducermanager.offer.Offer;

public class Order {
    private Offer offer;
    private double productAmount;  // Amount of product to be ordered.
    private String shopID; // ProductShop which places the order.

    public Order(Offer offer, double productAmount, String shopID) {
        this.offer = offer;
        this.productAmount = productAmount;
        this.shopID = shopID;
    }

    public JSONObject toJSONObject() {
        JSONObject orderJSONObject = new JSONObject();
        orderJSONObject.put("offer", getOffer().toJSONObject());
        orderJSONObject.put("shopID", getShopID());
        orderJSONObject.put("productAmount", getProductAmount());
        return orderJSONObject;
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
