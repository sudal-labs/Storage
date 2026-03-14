package com.sudal.storage.domain.repository

import com.sudal.storage.domain.entity.FileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FileRepository: JpaRepository<FileEntity, String> {

    @Modifying
    @Query("""
        UPDATE FileEntity f
        SET f.hasThumbnail = :hasThumbnail
        WHERE f.id = :fileId
    """)
    fun updateThumbnailStatus(fileId: String, hasThumbnail: Boolean): Int
}