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
package dev.kezz.miniphrase.i18n;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A registry of translations.
 *
 * @since 1.0.0
 */
@FunctionalInterface
public interface TranslationRegistry {

  /**
   * The empty translation registry.
   *
   * @return the empty translation registry
   * @since 1.0.0
   */
  @Contract(pure = true)
  static @NotNull TranslationRegistry empty() {
    return EmptyTranslationRegistry.INSTANCE;
  }

  /**
   * Returns a translation registry backed by the provided map.
   *
   * @param map the backing map
   * @return a translation registry
   * @since 1.0.0
   */
  @Contract(pure = true)
  static @NotNull TranslationRegistry fromMap(final @NotNull Map<@NotNull Locale, @NotNull Map<@NotNull String, @NotNull String>> map) {
    return new MapTranslationRegistry(Objects.requireNonNull(map, "map"));
  }

  /**
   * Returns a translation for a given key in a specific locale.
   *
   * <p>Note that the registry is not in charge of falling back to the default locale if
   * no translation key exists for the locale provided.</p>
   *
   * @param key the translation key
   * @param locale the locale
   * @return a translation, or {@code null} if no translation exists for this key and locale
   * @since 1.0.0
   */
  @Contract(pure = true)
  @Nullable
  String get(final @NotNull String key, final @NotNull Locale locale);
}
