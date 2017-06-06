package vs.productproducermanager.request;

import org.json.simple.JSONObject;

/**
 * Created by franky3er on 01.06.17.
 */
public class Request {
    private String shopID;
    private String productName;

    public Request(String shopID, String productName) {
        this.shopID = shopID;
        this.productName = productName;
    }

    public String getShopID() {
        return shopID;
    }

    public String getProductName() {
        return productName;
    }

    public JSONObject toJSONObject() {
        JSONObject requestJSONObject = new JSONObject();
        requestJSONObject.put("shopID", shopID);
        requestJSONObject.put("productName", productName);
        return requestJSONObject;
    }

    public String toJSONString() {
        return this.toJSONObject().toJSONString();
    }
}
