package Exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tusharkarkera on 10/26/14.
 */
    @SuppressWarnings("serial")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class InvalidRequestException extends RuntimeException {
        private Errors errors;

        public InvalidRequestException(String message, Errors errors) {
            super(message);
            this.errors = errors;
        }

        public Errors getErrors() { return errors; }
    }

