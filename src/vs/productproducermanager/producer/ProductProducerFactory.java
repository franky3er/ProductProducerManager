package vs.productproducermanager.producer;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import vs.productproducermanager.product.ProducedProduct;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProductProducerFactory {
    public static ProductProducer create(String jSONFilePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(jSONFilePath));
        return create(jsonObject);
    }

    private static ProductProducer create(JSONObject jSONObject) {
        String productProducerID = (String) jSONObject.get("productProducerID");
        Map<String, ProducedProduct> producedProducts= new HashMap<>();

        JSONArray prodProducts = (JSONArray) jSONObject.get("producedProducts");
        Iterator<JSONObject> iterator = prodProducts.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonProducedProductInformation = iterator.next();
            String productName = (String) jsonProducedProductInformation.get("productName");
            long productPricePerUnitMin = (Long) jsonProducedProductInformation.get("productPricePerUnitMin");
            long productPricePerUnitMax = (Long) jsonProducedProductInformation.get("productPricePerUnitMax");
            producedProducts.put(productName, new ProducedProduct(productName, productPricePerUnitMin, productPricePerUnitMax));
        }

        return new ProductProducer(productProducerID, producedProducts);
    }
}
