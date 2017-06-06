package vs.productproducermanager.producer;

import vs.productproducermanager.product.ProducedProduct;

import java.util.Map;

public class ProductProducer {
    private String productProducerID;
    private Map<String, ProducedProduct> producedProducts;

    public ProductProducer(String productProducerID, Map<String, ProducedProduct> producedProducts) {
        this.productProducerID = productProducerID;
        this.producedProducts = producedProducts;
    }

    public String getProductProducerID() {
        return productProducerID;
    }

    public Map<String, ProducedProduct> getProducedProducts() {
        return producedProducts;
    }
}
