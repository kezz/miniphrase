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
package dev.kezz.miniphrase.tag

import dev.kezz.miniphrase.MiniPhrase
import dev.kezz.miniphrase.MiniPhraseContext
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.Locale

/**
 * A wrapper around [TagResolver.Builder] with useful defaults, utility methods and
 * helper functions for MiniPhrase-specific elements.
 */
public class TagResolverBuilder private constructor(
  override val miniPhrase: MiniPhrase,
) : MiniPhraseContext {
  public companion object {
    private const val PHRASE_TAG_NAME: String = "phrase"

    /** Configures and builds a tag resolver using the provided builder and MiniPhrase instance. */
    public fun configureAndBuild(
      miniPhrase: MiniPhrase,
      builder: TagResolverBuilder.() -> Unit,
    ): TagResolver = TagResolverBuilder(miniPhrase).apply(builder).build()
  }

  private val builder: TagResolver.Builder = TagResolver.builder()

  /** Adds a parsed placeholder to this builder. */
  public fun parsed(
    key: String,
    value: Any?,
  ) {
    parsed(key, value.toString())
  }

  /** Adds a parsed placeholder to this builder. */
  public fun parsed(
    key: String,
    value: String,
  ) {
    tag(key, Tag.preProcessParsed(value))
  }

  /** Adds a parsed placeholder to this builder. The value is lazily computed. */
  public fun parsed(
    key: String,
    value: () -> String,
  ) {
    tag(key) { _, _ ->
      Tag.preProcessParsed(value())
    }
  }

  /** Adds an unparsed placeholder to this builder. */
  public fun unparsed(
    key: String,
    value: Any?,
  ) {
    unparsed(key, value.toString())
  }

  /** Adds an unparsed placeholder to this builder. */
  public fun unparsed(
    key: String,
    value: String,
  ) {
    tag(key, Tag.inserting(Component.text(value)))
  }

  /** Adds an unparsed placeholder to this builder. The value is lazily computed. */
  @JvmName("unparsedAny")
  public fun unparsed(
    key: String,
    value: () -> Any?,
  ) {
    unparsed(key) { value().toString() }
  }

  /** Adds an unparsed placeholder to this builder. The value is lazily computed. */
  public fun unparsed(
    key: String,
    value: () -> String,
  ) {
    tag(key) { _, _ ->
      Tag.inserting(Component.text(value()))
    }
  }

  /** Adds a tag that inserts a component. */
  public fun inserting(
    key: String,
    value: Component,
  ) {
    tag(key, Tag.inserting(value))
  }

  /** Adds a tag to this builder. */
  public fun tag(
    key: String,
    tag: Tag,
  ) {
    builder.tag(key, tag)
  }

  /** Constructs a tag and adds it to this builder. */
  public fun tag(
    key: String,
    tag: (arguments: ArgumentQueue, ctx: Context) -> Tag,
  ) {
    builder.tag(key, tag)
  }

  /** Adds a tag resolver to this builder. */
  public fun resolver(tagResolver: TagResolver) {
    builder.resolver(tagResolver)
  }

  /** Adds the phrase tag resolver to this builder, optionally with an overridden default locale. */
  public fun withPhraseTag(locale: Locale? = null) {
    tag(PHRASE_TAG_NAME) { arguments, ctx ->
      val key = arguments.popOr("No key provided.").lowerValue()
      val targetLocale =
        arguments.peek()?.let { arg ->
          runCatching { Locale.forLanguageTag(arg.lowerValue()) }.getOrElse { exception ->
            throw ctx.newException("Invalid language tag $arg.", exception, arguments)
          }
        } ?: locale ?: miniPhrase.defaultLocale

      val result =
        miniPhrase.translationRegistry[key, targetLocale]
          ?: miniPhrase.translationRegistry[key, miniPhrase.defaultLocale]

      if (result == null) {
        Tag.inserting(Component.text(key))
      } else {
        Tag.preProcessParsed(result)
      }
    }
  }

  /** Creates a tag resolver from this builder. */
  public fun build(): TagResolver = builder.build()
}
