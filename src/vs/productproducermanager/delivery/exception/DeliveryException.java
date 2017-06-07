package vs.productproducermanager.delivery.exception;

public class DeliveryException extends Exception {
    public DeliveryException(Exception e) {
        super(e.getMessage(), e);
    }
}
