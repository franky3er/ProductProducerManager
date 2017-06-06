package vs.productproducermanager.request.exception;

/**
 * Created by franky3er on 01.06.17.
 */
public class RequestException extends Exception {
    public RequestException(Exception e) {
        super(e.getMessage(), e);
    }
}
