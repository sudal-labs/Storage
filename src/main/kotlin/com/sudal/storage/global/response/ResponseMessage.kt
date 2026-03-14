package com.sudal.storage.global.response

enum class ResponseMessage(
    val status: Int,
    val message: String,
    val description: String
) {
    // 2xx Success
    OK(200, "OK", "성공"),
    CREATED(201, "CREATED", "생성"),
    NO_CONTENT(204, "NO_CONTENT", "반영"),

    // 4xx Client Error
    BAD_REQUEST(400, "BAD_REQUEST", "잘못된 요청"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "인증되지 않음"),
    FORBIDDEN(403, "FORBIDDEN", "접근 불가"),
    NOT_FOUND(404, "NOT_FOUND", "리소스를 찾을 수 없음"),
    METHOD_NOT_ALLOWED(405, "NOT_ALLOWED", "지원하지 않는 메소드"),

    // 5xx Server Error
    INTERNAL_ERROR(500, "INTERNAL_ERROR", "서버 에러")
}