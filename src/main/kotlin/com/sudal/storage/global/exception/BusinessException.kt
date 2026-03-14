package com.sudal.storage.global.exception

open class BusinessException(
    val errorObject: ErrorObject,
    override val message: String = errorObject.description
): RuntimeException(message)