package com.sudal.storage.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "files")
class FileEntity(

    @Id
    @Column(length = 36)
    val id: String,

    @Column(name = "original_name", nullable = false)
    val originalName: String,

    @Column(name = "stored_name", nullable = false)
    val storedName: String,

    @Column(name = "mime_type", length = 100, nullable = false)
    val mimeType: String,

    @Column(nullable = false)
    val size: Long,

    @Column
    var width: Int = 0,

    @Column
    var height: Int = 0,

    @Column(name = "has_thumbnail")
    var hasThumbnail: Boolean = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun updateDimensions(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun enableThumbnail() {
        this.hasThumbnail = true
    }

    fun disableThumbnail() {
        this.hasThumbnail = false
    }
}