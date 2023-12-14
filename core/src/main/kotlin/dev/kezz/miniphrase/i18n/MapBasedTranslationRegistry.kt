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

/** A translation registry that is backed by a map populated by a supplier. */
public open class MapBasedTranslationRegistry(
  /** The supplier of content for the map, used in reloads. */
  private val supplier: () -> Map<String, Map<String, String>>
) : TranslationRegistry {
  private var map: Map<String, Map<String, String>> = mapOf()

  override fun reload() {
    map = supplier()
  }

  override fun get(key: String, locale: String): String? =
    map[locale]?.get(key)

  override fun getTranslationList(key: String, locale: String): List<String> =
    (get(key, locale) ?: key).split("\n")

  override fun getLocales(): Set<String> = map.keys
}
