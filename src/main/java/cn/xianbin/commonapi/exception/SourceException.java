package cn.xianbin.commonapi.exception;

public class SourceException extends RuntimeException {
    public SourceException() {
        super();
    }

    public SourceException(String message) {
        super(message);
    }

    public SourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SourceException(Throwable cause) {
        super(cause);
    }

    protected SourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
