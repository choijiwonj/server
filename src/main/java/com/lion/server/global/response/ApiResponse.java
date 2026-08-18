package com.lion.server.global.response;

/**
 * 공통 API 응답 래퍼 Record
 *
 * @param success 요청 성공 여부
 * @param data    응답 페이로드 데이터
 * @param message 응답 메시지
 */
public record ApiResponse<T>(
        boolean success,
        T data,
        String message
) {
    /**
     * 성공 응답 (기본 메시지)
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, "요청이 성공적으로 처리되었습니다.");
    }

    /**
     * 성공 응답 (커스텀 메시지)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, data, message);
    }

    /**
     * 반환 데이터가 없는 성공 응답 (Record 컴포넌트명과 충돌 방지를 위해 ok 사용)
     */
    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(true, null, "요청이 성공적으로 처리되었습니다.");
    }

    /**
     * 에러 응답
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, message);
    }
}