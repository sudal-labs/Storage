package com.sudal.storage.global.handler

import com.sudal.storage.global.exception.FileErrorCode
import com.sudal.storage.global.exception.BusinessException
import com.sudal.storage.global.response.ApiResponse
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException

private val log = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    private fun handleBusinessException(ex: BusinessException): ResponseEntity<ApiResponse<Unit>> {
        val error = ex.errorObject
        log.warn{ "[BusinessException] Code: ${error.code} | Message: ${ex.message}" }

        return ResponseEntity
            .status(error.status)
            .body(ApiResponse.of(ex.message, error.status, null))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    private fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ApiResponse<Unit>> {
        val message = ex.bindingResult.allErrors.firstOrNull()?.defaultMessage ?: "잘못된 요청입니다"
        log.warn { "[ValidationException] $message" }

        return ResponseEntity
            .status(400)
            .body(ApiResponse.badRequest(message))
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    private fun handleMaxUploadSizeExceeded(
        ex: MaxUploadSizeExceededException
    ): ResponseEntity<ApiResponse<Unit>> {
        log.warn { "[MaxUploadSizeExceededException] ${ex.message}" }

        return ResponseEntity
            .status(400)
            .body(ApiResponse.badRequest(FileErrorCode.FILE_TOO_LARGE.description))
    }

    @ExceptionHandler(Exception::class)
    private fun handleException(ex: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error(ex) { "[UnhandledException] ${ex.message}" }

        return ResponseEntity
            .status(500)
            .body(ApiResponse.error("시스템 내부 오류가 발생했습니다."))
    }
}