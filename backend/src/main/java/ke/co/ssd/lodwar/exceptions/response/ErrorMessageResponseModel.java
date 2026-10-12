package ke.co.ssd.lodwar.exceptions.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorMessageResponseModel {

    private String message;
}
