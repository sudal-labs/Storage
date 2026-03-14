package com.sudal.storage.global.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val message: String,
    val status: Int,
    val data: T? = null
) {
    companion object {
        // ========== Success 200 ==========
        fun <T> success(): ApiResponse<T> = build(ResponseMessage.OK, null)

        fun <T> success(data: T): ApiResponse<T> = build(ResponseMessage.OK, data)

        fun success(message: String): ApiResponse<Unit> = build(ResponseMessage.OK, null, message)

        fun <T> success(data: T, message: String): ApiResponse<T> = build(ResponseMessage.OK, data, message)

        // ========== Read 200 ==========
        fun <T> read(data: T): ApiResponse<T> = build(ResponseMessage.OK, data)

        // ========== Create 201 ==========
        fun <T> create(): ApiResponse<T> = build(ResponseMessage.CREATED, null)

        fun <T> create(data: T): ApiResponse<T> = build(ResponseMessage.CREATED, data)

        fun create(message: String): ApiResponse<Unit> = build(ResponseMessage.CREATED, null, message)

        // ========== Delete 204 ==========
        fun <T> delete(): ApiResponse<T> = build(ResponseMessage.NO_CONTENT, null)

        // ========== Bad Request 400 ==========
        fun <T> badRequest(): ApiResponse<T> = build(ResponseMessage.BAD_REQUEST, null)

        fun badRequest(message: String): ApiResponse<Unit> = build(ResponseMessage.BAD_REQUEST, null, message)

        fun <T> badRequest(data: T, message: String): ApiResponse<T> = build(ResponseMessage.BAD_REQUEST, data, message)

        // ========== Not Found 404 ==========
        fun <T> notFound(): ApiResponse<T> = build(ResponseMessage.NOT_FOUND, null)

        fun notFound(message: String): ApiResponse<Unit> = build(ResponseMessage.NOT_FOUND, null, message)

        // ========== Unauthorized 401 ==========
        fun <T> unauthorized(message: String): ApiResponse<T> = build(ResponseMessage.UNAUTHORIZED, null, message)

        // ========== Forbidden 403 ==========
        fun <T> forbidden(message: String): ApiResponse<T> = build(ResponseMessage.FORBIDDEN, null, message)

        // ========== Error 500 ==========
        fun <T> error(): ApiResponse<T> = build(ResponseMessage.INTERNAL_ERROR, null)

        fun error(message: String): ApiResponse<Unit> = build(ResponseMessage.INTERNAL_ERROR, null, message)

        fun <T> error(data: T, message: String): ApiResponse<T> = build(ResponseMessage.INTERNAL_ERROR, data, message)

        // ========== Custom ==========
        fun <T> of(message: String, status: Int, data: T?): ApiResponse<T> =
            ApiResponse(message, status, data)

        // ========== Builder ==========
        private fun <T> build(responseMessage: ResponseMessage, data: T?): ApiResponse<T> =
            ApiResponse(responseMessage.message, responseMessage.status, data)

        private fun <T> build(responseMessage: ResponseMessage, data: T?, message: String): ApiResponse<T> =
            ApiResponse(message, responseMessage.status, data)
    }
}