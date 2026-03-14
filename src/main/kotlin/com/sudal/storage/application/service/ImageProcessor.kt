package com.sudal.storage.application.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import net.coobird.thumbnailator.Thumbnails
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

private val log = KotlinLogging.logger {}

data class ImageDimensions(val width: Int, val height: Int)

@Component
class ImageProcessor(

    @Value("\${image.thumbnail.width}")
    private val thumbnailWidth: Int,

    @Value("\${image.thumbnail.height}")
    private val thumbnailHeight: Int,

    @Value("\${image.thumbnail.quality}")
    private val thumbnailQuality: Double,
) {
    fun getDimensions(filePath: Path): ImageDimensions? {
        return try {
            val image: BufferedImage = ImageIO.read(filePath.toFile())
            ImageDimensions(image.width, image.height)
        } catch (e: Exception) {
            log.warn(e) { "이미지 크기 조회 실패: $filePath" }
            null
        }
    }

    suspend fun generateThumbnail(srcPath: Path, dstPath: Path): Boolean = withContext(Dispatchers.IO) {
        try {
            Thumbnails.of(srcPath.toFile())
                .size(thumbnailWidth, thumbnailWidth)
                .outputQuality(thumbnailQuality)
                .toFile(dstPath.toFile())

            log.debug { "썸네일 생성 완료: $dstPath" }
            true
        } catch (e: Exception) {
            log.error(e) { "썸네일 생성 실패: $srcPath -> $dstPath" }
            false
        }
    }
}