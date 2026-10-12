package ke.co.ssd.lodwar.config;

import ke.co.ssd.lodwar.exceptions.EntityNotFoundException;
import ke.co.ssd.lodwar.exceptions.ServiceException;
import ke.co.ssd.lodwar.exceptions.UnprocessableEntityException;
import ke.co.ssd.lodwar.exceptions.response.ErrorMessageResponseModel;
import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;
import ke.co.ssd.lodwar.exceptions.response.ErrorResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(final ServiceException ex, final WebRequest request) {

        if (ex instanceof EntityNotFoundException) {

            return handleExceptionInternal(ex, buildErrorResponse(ex), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
        }

        if (ex instanceof UnprocessableEntityException) {

            return handleExceptionInternal(ex, buildErrorResponse(ex), new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
        }

        return handleExceptionInternal(ex, buildErrorResponse(ex), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException exception) {

        return new ResponseEntity<>(buildErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatusCode status,
                                                                   final WebRequest request) {

        final String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        final ErrorResponseModel errorResponseModel = ErrorResponseModel.of(ErrorResponseEnum.TICKET_VALIDATION_FAILED, message);

        return new ResponseEntity<>(errorResponseModel, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(final Throwable exception) {

        log.error(ExceptionUtils.getStackTrace(exception));

        return new ResponseEntity<>(buildErrorResponse(exception.getClass().getName()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponseModel buildErrorResponse(final ServiceException exception) {

        return ErrorResponseModel.of(exception.retrieveExceptionCode(), exception.getMessage());
    }

    private ErrorMessageResponseModel buildErrorResponse(final String message) {

        return ErrorMessageResponseModel.builder()
            .message(message)
            .build();
    }

}
