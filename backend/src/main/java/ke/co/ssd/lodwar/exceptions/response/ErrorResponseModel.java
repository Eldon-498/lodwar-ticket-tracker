package ke.co.ssd.lodwar.exceptions.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponseModel {

    private ErrorResponseEnum code;

    private String message;

    public static ErrorResponseModel of(final ErrorResponseEnum code, final String message) {

        return ErrorResponseModel.builder()
            .code(code)
            .message(message)
            .build();
    }

}
