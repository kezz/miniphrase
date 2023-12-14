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

import java.util.Locale

/** A registry of translations. */
public interface TranslationRegistry {
  /** Loads or reloads all translations. */
  public fun reload()

  /** Returns a translation for a given [key] in a [locale]. */
  public operator fun get(
    key: String,
    locale: Locale,
  ): String?

  /**
   * Returns a translation list for a given [key] in a [locale].
   * List members are split with "\n" inside a translation.
   */
  public fun getList(
    key: String,
    locale: Locale,
  ): List<String> = get(key, locale)?.split("\n") ?: listOf(key)

  /** Returns the list of registered locales. */
  public fun getLocales(): Set<Locale>
}
