package vs.productproducermanager.request;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.request.exception.RequestException;

public class RequestFactory {
    public static Request create(String jSONString) throws ParseException, RequestException {
        JSONParser parser = new JSONParser();
        JSONObject requestJSONObject = (JSONObject) parser.parse(jSONString);
        return create(requestJSONObject);
    }

    private static Request create(JSONObject requestJSONObject) throws RequestException {
        try {
            String shopID = (String) requestJSONObject.get("shopID");
            String productName = (String) requestJSONObject.get("productName");
            return new Request(shopID, productName);
        } catch (ClassCastException e) {
            throw new RequestException(e);
        }
    }
}
