package com.sudal.storage.global.exception

interface ErrorObject {
    val status: Int
    val code: String
    val description: String
}