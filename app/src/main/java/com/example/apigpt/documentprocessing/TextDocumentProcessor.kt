package com.example.apigpt.documentprocessing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.io.File
import java.io.FileInputStream
import java.util.*

class TextDocumentProcessor {
    @Composable
    fun extractContentFromTextDocument(file: File): String {
        val content = remember { StringBuilder() }

        try {
            val fis = FileInputStream(file)
            val scanner = Scanner(fis)

            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n")
            }

            fis.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return content.toString()
    }
}
