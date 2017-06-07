package vs.productproducermanager.offer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by franky3er on 01.06.17.
 */
public class OfferFactory {
    public static Offer create(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject offerJSONObject = (JSONObject) parser.parse(jsonString);
        return create(offerJSONObject);
    }

    public static Offer create(JSONObject jSONObject) {
        int offerID = (Integer) jSONObject.get("offerID");
        String producerID = (String) jSONObject.get("producerID");
        String productName = (String) jSONObject.get("productName");
        long productPricePerUnit = (Long) jSONObject.get("productPricePerUnit");
        return new Offer(offerID, producerID, productName, productPricePerUnit);
    }
}
