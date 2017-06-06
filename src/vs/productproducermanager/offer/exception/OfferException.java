package vs.productproducermanager.offer.exception;

public class OfferException extends Exception {
    public OfferException(Exception e) {
        super(e.getMessage(), e);
    }
}
