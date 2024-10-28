package com.example.sns.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T> {

    private String resultCode;
    private T result;

    @Builder
    private Response(String resultCode, T result) {
        this.resultCode = resultCode;
        this.result = result;
    }

    public static Response<Void> error(String errorCode) {
        return Response.<Void>builder()
                .resultCode(errorCode)
                .result(null)
                .build();
    }

    public static <T> Response<T> success(T result) {
        return Response.<T>builder()
                .resultCode("SUCCESS")
                .result(result)
                .build();
    }

    public static Response<Void> success() {
        return Response.<Void>builder()
                .resultCode("SUCCESS")
                .result(null)
                .build();
    }

    public String toStream() {
        if (result == null) {
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null +
                    "}";
        }
        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + "\"" + result + "\"," +
                "}";
    }
}
