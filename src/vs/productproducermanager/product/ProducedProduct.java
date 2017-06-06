package vs.productproducermanager.product;

public class ProducedProduct {
    private String productName;
    private long productPricePerUnitMin;
    private long productPricePerUnitMax;

    public ProducedProduct(String productName, long productPricePerUnitMin, long productPricePerUnitMax) {
        this.productName = productName;
        this.productPricePerUnitMin = productPricePerUnitMin;
        this.productPricePerUnitMax = productPricePerUnitMax;
    }

    public String getProductName() {
        return productName;
    }

    public long getProductPricePerUnitMin() {
        return productPricePerUnitMin;
    }

    public long getProductPricePerUnitMax() {
        return productPricePerUnitMax;
    }
}
