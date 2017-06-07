package vs.productproducermanager.order.exception;

public class OrderException extends Exception {
    public OrderException(Exception e) {
        super(e.getMessage(), e);
    }
}
