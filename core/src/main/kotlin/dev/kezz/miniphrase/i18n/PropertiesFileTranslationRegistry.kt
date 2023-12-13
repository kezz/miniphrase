/*
 * MIT License
 *
 * Copyright (c) 2022 Kezz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.kezz.miniphrase.i18n

import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.*

/**
 * A translation registry that is populated by looking for property files in [path].
 * Format: en.properties
 */
public class PropertiesFileTranslationRegistry(
  private val path: File,
) : MapBasedTranslationRegistry({
  buildMap {
    if (path.exists() && !path.isDirectory) return@buildMap

    if (!path.exists()) path.mkdirs()

    Locale.getAvailableLocales().map { it.language }.distinct().forEach { languageKey ->
      val translationsFile = File(path, "$languageKey.properties")

      // If the file doesn't exist we check for the file in the resources and copy it
      // if the file exists there.
      if (!translationsFile.exists()) {
        javaClass.getResourceAsStream("/$languageKey.properties")?.let {
          try {
            translationsFile.createNewFile()

            Files.copy(
              it,
              translationsFile.getAbsoluteFile().toPath(),
              StandardCopyOption.REPLACE_EXISTING
            )
          } catch (exception: Exception) {
            exception.printStackTrace()
          }
        }
      }

      // If there is a file we read its contents.
      if (translationsFile.exists()) {
        val inputStream = FileInputStream(translationsFile)

        val properties = Properties()

        properties.load(inputStream)
        inputStream.close()

        put(languageKey,
          properties.stringPropertyNames().associateWith { key -> properties.getProperty(key) })
      }
    }
  }
})
