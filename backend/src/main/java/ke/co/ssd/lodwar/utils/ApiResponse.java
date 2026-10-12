package ke.co.ssd.lodwar.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private static final Integer SUCCESS_CODE = 1;

    private Status status;
    private T data;
    private Long totalItems;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Status {
        private Integer code;
        private String message;
    }

    public static <T> ApiResponse<List<T>> pagedResponse(final Page<T> page, final String message) {

        return ApiResponse.<List<T>>builder()
            .status(Status.builder().code(SUCCESS_CODE).message(message).build())
            .data(page.getContent())
            .totalItems(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber())
            .pageSize(page.getSize())
            .build();
    }

    public static <T> ApiResponse<T> success(final T data, final String message) {

        return ApiResponse.<T>builder()
            .status(Status.builder().code(SUCCESS_CODE).message(message).build())
            .data(data)
            .build();
    }

    public static <T> ApiResponse<T> fail(final Integer code, final String message, final T details) {

        return ApiResponse.<T>builder()
            .status(Status.builder().code(code).message(message).build())
            .data(details)
            .build();
    }
}
