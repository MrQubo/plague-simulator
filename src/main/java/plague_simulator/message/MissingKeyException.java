package plague_simulator.message;

public class MissingKeyException extends RuntimeException {
    static private final long serialVersionUID = 1L;


    public MissingKeyException() {
        super();
    }

    public MissingKeyException(String message) {
        super(message);
    }

    public MissingKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingKeyException(Throwable cause) {
        super(cause);
    }
}
