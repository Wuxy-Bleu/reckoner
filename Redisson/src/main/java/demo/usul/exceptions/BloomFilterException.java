package demo.usul.exceptions;

public class BloomFilterException extends RuntimeException {

    public BloomFilterException() {
        super();
    }

    public BloomFilterException(String message) {
        super(message);
    }

    public BloomFilterException(Throwable cause) {
        super(cause);
    }
}
