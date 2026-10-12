package ke.co.ssd.lodwar.exceptions;

import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;

public class ServiceException extends RuntimeException {

    private final ErrorResponseEnum code;

    public ServiceException(final String message, final ErrorResponseEnum code) {

        super(message);
        this.code = code;
    }

    public ErrorResponseEnum retrieveExceptionCode() {

        return this.code;
    }
}
