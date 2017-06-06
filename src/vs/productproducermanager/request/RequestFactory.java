package vs.productproducermanager.request;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestFactory {
    public static Request create(String jSONString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject requestJSONObject = (JSONObject) parser.parse(jSONString);
        return create(requestJSONObject);
    }

    private static Request create(JSONObject requestJSONObject) {
        String shopID = (String) requestJSONObject.get("shopID");
        String productName = (String) requestJSONObject.get("productName");
        return new Request(shopID, productName);
    }
}
