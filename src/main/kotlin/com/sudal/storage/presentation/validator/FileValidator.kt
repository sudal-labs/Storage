package com.sudal.storage.presentation.validator

import com.sudal.storage.global.exception.emptyFile
import com.sudal.storage.global.exception.fileTooLarge
import com.sudal.storage.global.exception.invalidExtension
import com.sudal.storage.global.exception.invalidMimeType
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.net.URLConnection

@Component
class FileValidator(

    @Value("\${storage.max-file-size}")
    private val maxFileSize: Long,

    @Value("\${image.allowed-types}")
    private val allowedTypes: List<String>,

    @Value("\${image.allowed-extensions}")
    private val allowedExtensions: List<String>
) {

    fun validate(file: MultipartFile) {
        if (file.isEmpty || file.size == 0L) {
            throw emptyFile()
        }

        if (file.size > maxFileSize) {
            throw fileTooLarge()
        }

        val extension = getExtension(file.originalFilename ?: "")
        if (!isAllowedExtension(extension)) {
            throw invalidExtension()
        }

        val mimeType = getMimeType(file)
        if (!isAllowedMimeType(mimeType)) {
            throw invalidMimeType()
        }
    }

    fun getExtension(fileName: String): String {
        val lastDot = fileName.lastIndexOf('.')
        return if (lastDot > 0) fileName.substring(lastDot).lowercase() else ""
    }

    fun getMimeType(file: MultipartFile): String {
        val detectedType = URLConnection.guessContentTypeFromStream(file.inputStream)
            ?: file.contentType
            ?: "application/octet-stream"

        return detectedType.split(";").first().trim()
    }

    private fun isAllowedExtension(extension: String): Boolean {
        return allowedExtensions.any { it.equals(extension, ignoreCase = true) }
    }

    private fun isAllowedMimeType(mimeType: String): Boolean {
        return allowedTypes.any { it.equals(mimeType, ignoreCase = true) }
    }
}