package ke.co.ssd.lodwar.exceptions;

import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;

public class EntityNotFoundException extends ServiceException {

    public EntityNotFoundException(final String message, final ErrorResponseEnum code) {

        super(message, code);
    }
}
