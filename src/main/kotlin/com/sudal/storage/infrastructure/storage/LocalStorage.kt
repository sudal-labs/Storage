package com.sudal.storage.infrastructure.storage

import com.sudal.storage.global.exception.fileNotFound
import com.sudal.storage.global.exception.storageError
import jakarta.annotation.PostConstruct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

private val log = KotlinLogging.logger {}

interface Storage {
    fun save(fileName: String, inputStream: InputStream): Path
    fun load(fileName: String): Resource
    fun delete(fileName: String): Boolean
    fun exists(fileName: String): Boolean
    fun getFilePath(fileName: String): Path
    fun getThumbnailPath(fileName: String): Path
}

@Component
class LocalStorage(
    @Value("\${storage.base-path}")
    private val basePath: String
) : Storage {

    private lateinit var rootPath: Path
    private lateinit var thumbnailPath: Path

    @PostConstruct
    fun init() {
        rootPath = Paths.get(basePath).toAbsolutePath().normalize()
        thumbnailPath = rootPath.resolve("thumbnails")

        try {
            Files.createDirectories(rootPath)
            Files.createDirectories(thumbnailPath)

            log.info { "Storage initialized: $rootPath" }
        } catch (e: Exception) {
            throw storageError("스토리지 디렉토리 생성 실패")
        }
    }

    override fun save(fileName: String, inputStream: InputStream): Path {
        return try {
            val targetPath = rootPath.resolve(fileName).normalize()

            if (!targetPath.startsWith(rootPath)) {
                throw storageError("잘못된 파일 경로입니다.")
            }

            Files.createDirectories(targetPath.parent)
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
            log.debug { "File Saved: $targetPath" }

            targetPath
        } catch (e: Exception) {
            if (e.message?.contains("잘못된 파일 경로") == true) throw e
            throw storageError("파일 저장 실패: $fileName")
        }
    }

    fun save(fileName: String, file: MultipartFile): Path {
        return save(fileName, file.inputStream)
    }

    override fun load(fileName: String): Resource {
        return try {
            val filePath = rootPath.resolve(fileName).normalize()

            if (!filePath.startsWith(rootPath)) {
                throw fileNotFound("잘못된 파일 경로입니다.")
            }

            val resource = UrlResource(filePath.toUri())

            if (resource.exists() && resource.isReadable) {
                resource
            } else {
                throw fileNotFound("파일을 찾을 수 없습니다: $fileName")
            }
        } catch (e: Exception) {
            if (e.message?.contains("파일을 찾을 수 없습니다") == true) throw e
            throw fileNotFound("파일을 찾을 수 없습니다: $fileName")
        }
    }

    override fun delete(fileName: String): Boolean {
        return try {
            val filePath = rootPath.resolve(fileName).normalize()

            if (!filePath.startsWith(rootPath)) {
                return false
            }

            val deleted = Files.deleteIfExists(filePath)
            if (deleted) {
                log.debug { "File deleted: $filePath" }
            }

            deleted
        } catch (e: Exception) {
            log.warn(e) { "파일 삭제 실패: $fileName" }
            false
        }
    }

    override fun exists(fileName: String): Boolean {
        val filePath = rootPath.resolve(fileName).normalize()
        return filePath.startsWith(rootPath) && Files.exists(filePath)
    }

    override fun getFilePath(fileName: String): Path {
        return rootPath.resolve(fileName).normalize()
    }

    override fun getThumbnailPath(fileName: String): Path {
        return thumbnailPath.resolve("thumb_$fileName").normalize()
    }
}