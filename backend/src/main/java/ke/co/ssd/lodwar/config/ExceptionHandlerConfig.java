package ke.co.ssd.lodwar.config;

import ke.co.ssd.lodwar.exceptions.ServiceException;
import ke.co.ssd.lodwar.exceptions.response.ErrorResponseEnum;
import ke.co.ssd.lodwar.utils.ApiResponse;
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
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionHandlerConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(final ServiceException ex) {

        return ResponseEntity.ok(buildErrorResponse(ex.retrieveExceptionCode(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(final IllegalArgumentException ex) {

        return ResponseEntity.ok(buildErrorResponse(ErrorResponseEnum.ERROR_DEFAULT_MESSAGE, ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatusCode status,
                                                                   final WebRequest request) {

        final String message = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + " " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.ok(buildErrorResponse(ErrorResponseEnum.TICKET_VALIDATION_FAILED, message));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleThrowable(final Throwable ex) {

        log.error(ExceptionUtils.getStackTrace(ex));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildErrorResponse(ErrorResponseEnum.ERROR_DEFAULT_MESSAGE, ex.getClass().getName()));
    }

    private ApiResponse<Object> buildErrorResponse(final ErrorResponseEnum code, final String message) {

        return ApiResponse.fail(code.getCode(), message, null);
    }

}
