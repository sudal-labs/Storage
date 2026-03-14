package com.sudal.storage.application.dto

import com.sudal.storage.domain.entity.FileEntity
import java.time.LocalDateTime

data class FileUploadData(
    val fileId: String,
    val originalName: String,
    val mimeType: String,
    val size: Long,
    val url: String,
    val thumbnailUrl: String?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(file: FileEntity, baseUrl: String): FileUploadData {
            return FileUploadData(
                fileId = file.id,
                originalName = file.originalName,
                mimeType = file.mimeType,
                size = file.size,
                url = "$baseUrl/api/v1/files/${file.id}",
                thumbnailUrl = if (file.hasThumbnail) "$baseUrl/api/v1/files/${file.id}/thumbnail" else null,
                createdAt = file.createdAt
            )
        }
    }
}

data class FileMetadataData(
    val fileId: String,
    val originalName: String,
    val storedName: String,
    val mimeType: String,
    val size: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val thumbnailUrl: String?,
    val hasThumbnail: Boolean,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun from(file: FileEntity, baseUrl: String): FileMetadataData {
            return FileMetadataData(
                fileId = file.id,
                originalName = file.originalName,
                storedName = file.storedName,
                mimeType = file.mimeType,
                size = file.size,
                width = file.width,
                height = file.height,
                url = "$baseUrl/api/v1/files/${file.id}",
                thumbnailUrl = if (file.hasThumbnail) "$baseUrl/api/v1/files/${file.id}/thumbnail" else null,
                hasThumbnail = file.hasThumbnail,
                createdAt = file.createdAt
            )
        }
    }
}

data class HealthData(
    val status: String = "OK",
    val service: String = "storage-service"
)
