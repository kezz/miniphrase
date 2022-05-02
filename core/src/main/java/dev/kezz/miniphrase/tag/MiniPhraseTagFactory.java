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
package dev.kezz.miniphrase.tag;

import dev.kezz.miniphrase.MiniPhrase;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Custom MiniMessage tags provided by MiniPhrase.
 *
 * @since 1.0.0
 */
@ApiStatus.NonExtendable
public class MiniPhraseTagFactory {
  private final MiniPhrase miniPhrase;

  public MiniPhraseTagFactory(final @NotNull MiniPhrase miniPhrase) {

  }

  /**
   * Returns an instance of the {@code <phrase:<key>:[locale]>} tag with a given default locale and
   * MiniPhrase instance.
   *
   * @since 1.0.0
   */
  static @NotNull TagResolver phrase(
      final @NotNull MiniPhrase miniPhrase, final @NotNull Locale locale) {
    return new PhraseTag(miniPhrase, locale);
  }
}
