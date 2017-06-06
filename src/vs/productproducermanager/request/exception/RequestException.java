package vs.productproducermanager.request.exception;

public class RequestException extends Exception {
    public RequestException(Exception e) {
        super(e.getMessage(), e);
    }
}
