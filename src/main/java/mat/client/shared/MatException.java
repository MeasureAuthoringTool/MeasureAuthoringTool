package mat.client.shared;

/**
 * The Class MatException.
 */
public class MatException extends Exception {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7520932982221949484L;

    public MatException() {
        super();
    }

    public MatException(String msg) {
        super(msg);
    }

    public MatException(Throwable cause) {
        super(cause);
    }

    public MatException(String message, Throwable cause) {
        super(message, cause);
    }
}
