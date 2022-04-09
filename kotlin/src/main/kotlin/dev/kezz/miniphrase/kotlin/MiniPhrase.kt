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
package dev.kezz.miniphrase.kotlin

import dev.kezz.miniphrase.MiniPhraseContext
import dev.kezz.miniphrase.kotlin.i18n.EmptyTranslationRegistry
import dev.kezz.miniphrase.kotlin.i18n.TranslationRegistry
import dev.kezz.miniphrase.kotlin.tag.GenericTagBuilder
import dev.kezz.miniphrase.kotlin.tag.MiniPhraseTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Locale

/** The main entry-point for the MiniPhrase library. */
public class MiniPhrase private constructor(
  /** The MiniMessage instance. */
  public val miniMessage: MiniMessage,
  /** The translation registry. */
  public val translationRegistry: TranslationRegistry,
  /** The default locale for translations. */
  public val defaultLocale: Locale,
  /** If the phrase tag should be included by default. */
  public val includePhraseTag: Boolean
) : MiniPhraseContext {

  public companion object {
    /** Creates a simple MiniPhrase instance from a given translation registry. */
    public fun fromTranslationRegistry(translationRegistry: TranslationRegistry): MiniPhrase =
      configureAndBuild { translationRegistry(translationRegistry) }

    /** Configures and builds a MiniPhrase instance using the provided [builder]. */
    public fun configureAndBuild(builder: Builder.() -> Unit): MiniPhrase =
      Builder().apply(builder).build()
  }

  /** This MiniPhrase instance. */
  override fun getMiniPhrase(): MiniPhrase = this

  /** Translates a key with a given locale, or the default locale. */
  public fun translate(
    /** The translation key. */
    key: String,
    /** The desired locale, or `null` to use the [defaultLocale]. */
    locale: Locale? = null,
    /** An additional tag resolver to use. */
    tags: TagResolver
  ): Component {
    val targetLocale = locale ?: defaultLocale
    val translationString = translationRegistry[key, targetLocale]
      ?: return Component.text(key) // If no translation was found, just return the key.
    val resolver =
      if (includePhraseTag) {
        MiniPhraseTags.build {
          withPhraseTag(targetLocale)
          resolver(tags)
        }
      } else {
        tags
      }

    return miniMessage.deserialize(translationString, resolver)
  }

  /** Translates a key with a given locale, or the default locale. */
  public fun translate(
    /** The translation key. */
    key: String,
    /** The desired locale, or `null` to use the [defaultLocale]. */
    locale: Locale? = null,
    /** A tag resolver builder to use, if any. */
    tags: GenericTagBuilder? = null
  ): Component {
    val targetLocale = locale ?: defaultLocale
    val translationString = translationRegistry[key, targetLocale]
      ?: return Component.text(key) // If no translation was found, just return the key.
    val resolver =
      if (!includePhraseTag && tags == null) {
        TagResolver.empty()
      } else {
        MiniPhraseTags.build {
          if (includePhraseTag) withPhraseTag(locale)
          if (tags != null) tags()
        }
      }

    return miniMessage.deserialize(translationString, resolver)
  }

  /** Builder class for MiniPhrase instances. */
  public class Builder {
    private var miniMessage: MiniMessage = MiniMessage.miniMessage()
    private var translationRegistry: TranslationRegistry = EmptyTranslationRegistry
    private var defaultLocale: Locale = Locale.getDefault()
    private var includePhraseTag: Boolean = false

    /** Sets the MiniMessage instance to use from an instance created by the provided builder. */
    public fun miniMessage(miniMessageBuilder: MiniMessage.Builder.() -> Unit): Builder {
      miniMessage = MiniMessage.builder().apply(miniMessageBuilder).build()
      return this
    }

    /** Sets the MiniMessage instance to use. */
    public fun miniMessage(miniMessage: MiniMessage): Builder {
      this.miniMessage = miniMessage
      return this
    }

    /** Sets the translation registry to use. */
    public fun translationRegistry(translationRegistry: TranslationRegistry): Builder {
      this.translationRegistry = translationRegistry
      return this
    }

    /** Sets the default locale, for when no receiver-specific locale is available. */
    public fun defaultLocale(locale: Locale): Builder {
      defaultLocale = locale
      return this
    }

    /** If the `<phrase:<key>:[locale]>` tag should be included in each translate by default. */
    public fun includePhraseTag(shouldInclude: Boolean): Builder {
      includePhraseTag = shouldInclude
      return this
    }

    /** Creates a MiniPhrase instance from this builder. */
    public fun build(): MiniPhrase =
      MiniPhrase(miniMessage, translationRegistry, defaultLocale, includePhraseTag)
  }
}
