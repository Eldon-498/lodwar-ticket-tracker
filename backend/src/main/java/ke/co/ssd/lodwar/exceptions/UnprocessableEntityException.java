package ke.co.ssd.lodwar.exceptions;

import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;

public class UnprocessableEntityException extends ServiceException {

    public UnprocessableEntityException(final String message, final ErrorResponseEnum code) {

        super(message, code);
    }
}
