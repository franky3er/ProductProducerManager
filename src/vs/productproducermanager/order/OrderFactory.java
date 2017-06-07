package vs.productproducermanager.order;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.offer.Offer;
import vs.productproducermanager.offer.OfferFactory;

public class OrderFactory {
    public static Order create(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject orderJSONObject = (JSONObject) parser.parse(jsonString);
        return create(orderJSONObject);
    }

    public static Order create(JSONObject jSONObject) {
        Offer offer = OfferFactory.create((JSONObject) jSONObject.get("offer"));
        String shopID = (String) jSONObject.get("shopID");
        double productAmount = (Double) jSONObject.get("productAmount");

        return new Order(offer, productAmount, shopID);
    }
}
