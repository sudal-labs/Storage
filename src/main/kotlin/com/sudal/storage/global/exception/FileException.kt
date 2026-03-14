package com.sudal.storage.global.exception

class FileException(
    errorCode: FileErrorCode,
    message: String = errorCode.description
): BusinessException(errorCode, message)

fun fileNotFound(message: String = FileErrorCode.FILE_NOT_FOUND.description) =
    FileException(FileErrorCode.FILE_NOT_FOUND, message)

fun thumbnailNotFound(message: String = FileErrorCode.THUMBNAIL_NOT_FOUND.description) =
    FileException(FileErrorCode.THUMBNAIL_NOT_FOUND, message)

fun emptyFile(message: String = FileErrorCode.EMPTY_FILE.description) =
    FileException(FileErrorCode.EMPTY_FILE, message)

fun fileTooLarge(message: String = FileErrorCode.FILE_TOO_LARGE.description) =
    FileException(FileErrorCode.FILE_TOO_LARGE, message)

fun invalidMimeType(message: String = FileErrorCode.INVALID_MIME_TYPE.description) =
    FileException(FileErrorCode.INVALID_MIME_TYPE, message)

fun invalidExtension(message: String = FileErrorCode.INVALID_EXTENSION.description) =
    FileException(FileErrorCode.INVALID_EXTENSION, message)

fun storageError(message: String = FileErrorCode.STORAGE_ERROR.description) =
    FileException(FileErrorCode.STORAGE_ERROR, message)
