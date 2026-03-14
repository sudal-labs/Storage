package com.sudal.storage.application.service

import com.sudal.storage.domain.repository.FileRepository
import com.sudal.storage.infrastructure.storage.LocalStorage
import com.sudal.storage.presentation.validator.FileValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class FileService(
    private val fileRepository: FileRepository,
    private val storage: LocalStorage,
    private val imageProcessor: ImageProcessor,
    private val fileValidator: FileValidator,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
}