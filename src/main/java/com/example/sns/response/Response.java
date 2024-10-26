package com.example.sns.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {

    private String statusCode;
    private T data;

    @Builder
    private Response(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public static Response<Void> error(String errorCode) {
        return Response.<Void>builder()
                .statusCode(errorCode)
                .data(null)
                .build();
    }

    public static <T> Response<T> success(T result) {
        return Response.<T>builder()
                .statusCode("SUCCESS")
                .data(result)
                .build();
    }
}
