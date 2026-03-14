package com.sudal.storage.global.exception

enum class FileErrorCode(
    override val status: Int,
    override val code: String,
    override val description: String,
): ErrorObject {
    // 400 Bad Request
    EMPTY_FILE(400, "FILE_001", "파일이 비어있습니다"),
    FILE_TOO_LARGE(400, "FILE_002", "파일 크기가 10MB를 초과합니다"),
    INVALID_MIME_TYPE(400, "FILE_003", "허용되지 않는 파일 형식입니다"),
    INVALID_EXTENSION(400, "FILE_004", "허용되지 않는 파일 확장자입니다"),

    // 404 Not Found
    FILE_NOT_FOUND(404, "FILE_005", "파일을 찾을 수 없습니다"),
    THUMBNAIL_NOT_FOUND(404, "FILE_006", "썸네일을 찾을 수 없습니다"),

    // 500 Internal Server Error
    STORAGE_ERROR(500, "FILE_007", "파일 저장에 실패했습니다"),
    PROCESSING_ERROR(500, "FILE_008", "파일 처리에 실패했습니다")
}