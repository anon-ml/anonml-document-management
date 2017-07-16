package ml.anon.documentmanagement.exception;

import java.util.Objects;

/**
 * Exceptions thrown on Document Management Problems
 * Created by mirco on 07.05.17.
 */
public class DocumentManagementException extends RuntimeException {
    public static final String UNSPECIFIED = "No specific message given";
    private final String message;

    public DocumentManagementException(Exception cause) {
        this(null, cause);
    }

    public DocumentManagementException(String message, Exception cause) {
        this.message = message == null? UNSPECIFIED : message;

        if(cause != null) {
            initCause(cause);
        }
    }


}
