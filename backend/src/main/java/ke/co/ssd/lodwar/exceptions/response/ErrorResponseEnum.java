package ke.co.ssd.lodwar.exceptions.response;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ErrorResponseEnum {

    ERROR_DEFAULT_MESSAGE(1001),
    TICKET_NOT_FOUND(2001),
    TICKET_VALIDATION_FAILED(2002),
    TICKET_UPDATE_NO_FIELDS(2003);

    private final Integer code;

    ErrorResponseEnum(final Integer code) {

        this.code = code;
    }

    @Override
    @JsonValue
    public String toString() {

        return Integer.toString(this.code);
    }
}
