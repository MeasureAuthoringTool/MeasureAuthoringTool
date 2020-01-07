package mat.client.shared;

public class MatRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 7520932982221949484L;

    public MatRuntimeException() {
        super();
    }

    public MatRuntimeException(String msg) {
        super(msg);
    }
}
