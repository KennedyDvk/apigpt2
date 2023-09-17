package com.example.apigpt.utils

import java.io.File

object FileUtils {
    fun scanSystemForFiles(directoryPath: String): Map<String, List<String>> {
        val fileHashMap = mutableMapOf<String, List<String>>()

        val directory = File(directoryPath)

        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()
            files?.forEach { file ->
                if (file.isFile) {
                    val extension = file.extension.toLowerCase()
                    val fileName = file.name

                    // Adicione o arquivo ao HashMap com base na extensão
                    if (fileHashMap.containsKey(extension)) {
                        val existingFiles = fileHashMap[extension] as MutableList<String>
                        existingFiles.add(fileName)
                    } else {
                        fileHashMap[extension] = mutableListOf(fileName)
                    }
                }
            }
        }

        return fileHashMap
    }
}
