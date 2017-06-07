package vs.productproducermanager.order;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.offer.Offer;
import vs.productproducermanager.offer.OfferFactory;
import vs.productproducermanager.offer.exception.OfferException;
import vs.productproducermanager.order.exception.OrderException;

public class OrderFactory {
    public static Order create(String jsonString) throws ParseException, OfferException, OrderException {
        JSONParser parser = new JSONParser();
        JSONObject orderJSONObject = (JSONObject) parser.parse(jsonString);
        return create(orderJSONObject);
    }

    public static Order create(JSONObject jSONObject) throws OfferException, OrderException {
        try {
            Offer offer = OfferFactory.create((JSONObject) jSONObject.get("offer"));
            String shopID = (String) jSONObject.get("shopID");
            double productAmount = (Double) jSONObject.get("productAmount");

            return new Order(offer, productAmount, shopID);
        } catch (ClassCastException e) {
            throw new OrderException(e);
        }
    }
}
