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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Context;
import net.kyori.adventure.text.minimessage.ParsingException;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

record PhraseTag(MiniPhrase miniPhrase, Locale defaultLocale) implements TagResolver {
  private static final String PHRASE = "phrase";

  @Override
  public @Nullable Tag resolve(
      final @NotNull String name,
      final @NotNull ArgumentQueue arguments,
      final @NotNull Context ctx)
      throws ParsingException {
    final var key = arguments.popOr("Missing key").lowerValue();
    final Locale targetLocale;

    if (arguments.hasNext()) {
      final var localeString = arguments.pop().lowerValue();

      try {
        targetLocale = Locale.forLanguageTag(localeString);
      } catch (final NullPointerException exception) {
        throw ctx.newException("Invalid language tag " + localeString, exception, arguments);
      }
    } else {
      targetLocale = this.defaultLocale;
    }

    final var translation = this.miniPhrase.getTranslationOrDefault(key, targetLocale);

    if (translation == null) {
      return Tag.inserting(Component.text(key));
    } else {
      return Tag.preProcessParsed(translation);
    }
  }

  @Override
  public boolean has(final @NotNull String name) {
    return PHRASE.equals(name);
  }
}
