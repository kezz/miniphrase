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
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

/**
 * The main entry-point for the MiniPhrase library.
 *
 * @since 1.0.0
 */
public sealed interface MiniPhrase permits MiniPhraseImpl {
  /**
   * Creates a new builder instance.
   *
   * @return a new builder
   * @since 1.0.0
   */
  @Contract(value = "-> new", pure = true)
  static @NotNull Builder builder() {
    return new MiniPhraseImpl.BuilderImpl();
  }

  /**
   * The MiniMessage instance.
   *
   * @return the MiniMessage instance
   * @since 1.0.0
   */
  @Contract(pure = true)
  @NotNull
  MiniMessage getMiniMessage();

  /**
   * The translation registry.
   *
   * @return the translation registry
   * @since 1.0.0
   */
  @Contract(pure = true)
  @NotNull
  TranslationRegistry getTranslationRegistry();

  /**
   * The default locale for translations.
   *
   * @return the default locale
   * @since 1.0.0
   */
  @Contract(pure = true)
  @NotNull
  Locale getDefaultLocale();

  /**
   * If the phrase tag should be included by default.
   *
   * @return if phrase tags should be included by default
   * @since 1.0.0
   */
  @Contract(pure = true)
  boolean isIncludingPhraseTag();

  /**
   * Gets the translation string for a given key and locale, falling back to the default locale if
   * no mapping was found for the provided locale.
   *
   * @return the translation string, or {@code null} if none are found
   * @since 1.0.0
   */
  @Contract(pure = true)
  @Nullable
  String getTranslationOrDefault(final @NotNull String key, final @NotNull Locale locale);

  /**
   * Creates a translated component given a key, an optional locale and a tag resolver.
   *
   * @param key the key of the translation
   * @param locale the locale to use, or {@code null} for {@link #getDefaultLocale()}
   * @param tagResolver an additional tag resolver to use, or {@code null} for {@link TagResolver#empty()}
   * @return the translated component
   * @since 1.0.0
   */
  @Contract(pure = true)
  @NotNull
  Component createComponent(
      final @NotNull String key,
      final @Nullable Locale locale,
      final @Nullable TagResolver tagResolver);

  /**
   * Creates a translated component for an audience member given a key, an optional locale and a tag
   * resolver supplier.
   *
   * @param key the key of the translation
   * @param locale the locale to use, or {@code null} for {@link #getDefaultLocale()}
   * @param recipient the recipient of the message
   * @param tagResolverProvider a provider of tag resolvers, given a recipient
   * @param <T> the type of the recipient
   * @return the translated component
   * @since 1.0.0
   */
  @Contract(pure = true)
  @NotNull
  <T extends Audience> Component createContextualComponent(
      final @NotNull String key,
      final @Nullable Locale locale,
      final @NotNull T recipient,
      final @NotNull Function<@NotNull T, @NotNull TagResolver> tagResolverProvider);

  /**
   * A builder of MiniPhrase instances.
   *
   * @since 1.0.0
   */
  sealed interface Builder permits MiniPhraseImpl.BuilderImpl {
    /**
     * Sets the MiniMessage instance to use.
     *
     * <p>If not set, this defaults to {@link MiniMessage#miniMessage()}.</p>
     *
     * @param miniMessage the MiniMessage instance
     * @return {@code this}, for chaining
     * @since 1.0.0
     */
    @Contract(value = "_ -> this", mutates = "this")
    @NotNull
    Builder setMiniMessage(final @NotNull MiniMessage miniMessage);

    /**
     * Sets the translation registry to use.
     *
     * <p>If not set, this defaults to {@link TranslationRegistry#empty()}.</p>
     *
     * @param translationRegistry the translation registry
     * @return {@code this}, for chaining
     * @since 1.0.0
     */
    @Contract(value = "_ -> this", mutates = "this")
    @NotNull
    Builder setTranslationRegistry(final @NotNull TranslationRegistry translationRegistry);

    /**
     * Sets the default locale to use.
     *
     * <p>If not set, this defaults to {@link Locale#getDefault()}.</p>
     *
     * @param locale the default locale
     * @return {@code this}, for chaining
     * @since 1.0.0
     */
    @Contract(value = "_ -> this", mutates = "this")
    @NotNull
    Builder setDefaultLocale(final @NotNull Locale locale);

    /**
     * Sets if the phrase tag should be included by default.
     *
     * <p>If not set, this defaults to {@code false}.</p>
     *
     * @param includePhraseTag if the phrase tag should be included by default
     * @since 1.0.0
     */
    @Contract(value = "_ -> this", mutates = "this")
    @NotNull
    Builder setIncludingPhraseTag(boolean includePhraseTag);

    /**
     * Builds a MiniPhrase instance from this builder.
     *
     * @return a new MiniPhrase instance
     * @since 1.0.0
     */
    @Contract(value = "-> new", pure = true)
    @NotNull
    MiniPhrase build();
  }
}
