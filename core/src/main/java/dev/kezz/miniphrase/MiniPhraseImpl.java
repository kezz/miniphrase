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
package dev.kezz.miniphrase;

import dev.kezz.miniphrase.i18n.TranslationRegistry;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Objects;

final class MiniPhraseImpl implements MiniPhrase {
  private final MiniMessage miniMessage;
  private final TranslationRegistry translationRegistry;
  private final Locale defaultLocale;
  private final boolean includingPhraseTag;

  MiniPhraseImpl(final @NotNull BuilderImpl builder) {
    this.miniMessage = builder.miniMessage;
    this.translationRegistry = builder.translationRegistry;
    this.defaultLocale = builder.defaultLocale;
    this.includingPhraseTag = builder.isIncludingPhraseTag;
  }

  @Override
  public @NotNull MiniMessage getMiniMessage() {
    return this.miniMessage;
  }

  @Override
  public @NotNull TranslationRegistry getTranslationRegistry() {
    return this.translationRegistry;
  }

  @Override
  public @NotNull Locale getDefaultLocale() {
    return this.defaultLocale;
  }

  @Override
  public boolean isIncludingPhraseTag() {
    return this.includingPhraseTag;
  }

  @Override
  public @Nullable String getTranslationOrDefault(
      final @NotNull String key, final @NotNull Locale locale) {
    final var translation =
        this.translationRegistry.get(
            Objects.requireNonNull(key, "key"), Objects.requireNonNull(locale, "locale"));

    if (translation != null) {
      return translation;
    } else {
      if (locale.equals(this.defaultLocale)) {
        return null;
      } else {
        return this.translationRegistry.get(key, this.defaultLocale);
      }
    }
  }

  static final class BuilderImpl implements Builder {
    private MiniMessage miniMessage = MiniMessage.miniMessage();
    private TranslationRegistry translationRegistry = TranslationRegistry.empty();
    private Locale defaultLocale = Locale.getDefault();
    private boolean isIncludingPhraseTag = false;

    @Override
    public @NotNull Builder setMiniMessage(final @NotNull MiniMessage miniMessage) {
      this.miniMessage = Objects.requireNonNull(miniMessage, "miniMessage");
      return this;
    }

    @Override
    public @NotNull Builder setTranslationRegistry(
        final @NotNull TranslationRegistry translationRegistry) {
      this.translationRegistry = Objects.requireNonNull(translationRegistry, "translationRegistry");
      return this;
    }

    @Override
    public @NotNull Builder setDefaultLocale(final @NotNull Locale locale) {
      this.defaultLocale = Objects.requireNonNull(locale, "locale");
      return this;
    }

    @Override
    public @NotNull Builder setIncludingPhraseTag(final boolean includePhraseTag) {
      this.isIncludingPhraseTag = includePhraseTag;
      return this;
    }

    @Override
    public @NotNull MiniPhrase build() {
      return new MiniPhraseImpl(this);
    }
  }
}
