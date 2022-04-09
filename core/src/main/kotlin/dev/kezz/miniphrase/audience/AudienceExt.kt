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
package dev.kezz.miniphrase.audience

import dev.kezz.miniphrase.MiniPhraseContext
import dev.kezz.miniphrase.tag.ContextualTagBuilder
import dev.kezz.miniphrase.tag.GenericTagBuilder
import dev.kezz.miniphrase.tag.asNonContextual
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

/**
 * Sends a message to this audience, with optional additional tags.
 *
 * The message will be translated using the provided [locale]. If this locale is null,
 * it will instead be translated using the locale provided from the pointers of the
 * audience. If this is not available, or they do not have a locale, the default locale
 * for the MiniPhrase instance in use will be used.
 *
 * This method will account for any forwarding audiences, automatically this method on
 * each child, as dictated by the [Audience.forEachAudience] method. If the locale that
 * is provided is not null, then this will be skipped as the resulting component is
 * fixed for all recipients of the message.
 *
 * There is no guarantee to the amount of times the [tags] builder will be built. If you
 * need different tags for each recipient, consider using
 * [Audience.sendTranslatedContextual].
 */
context(MiniPhraseContext)
public fun Audience.sendTranslated(
  /** The key of the message. */
  key: String,
  /** The type of the message. */
  type: MessageType = MessageType.CHAT,
  /** The identity of the message sender. */
  identity: Identity = Identity.nil(),
  /** The locale to translate the message in, if not the default for the audience. */
  locale: Locale? = null,
  /* A builder of additional tags to use in the deserialization process. */
  tags: GenericTagBuilder? = null
) {
  when {
    this == Audience.empty() -> {
      // Do nothing if this audience is the empty audience.
    }

    locale == null && this is ForwardingAudience -> {
      // We only run through each child if the locale is null (i.e. we're pulling it from the audience itself).
      forEachAudience { child ->
        // If the audience has decided to iterate on itself, we prevent a stack overflow here
        // by skipping this case.
        if (child !== this) {
          child.sendTranslated(key, type, identity, null, tags)
        }
      }
    }

    else -> {
      // If we don't have to iterate, just send directly to the audience member!
      val targetLocale = locale ?: get(Identity.LOCALE).orElseGet(miniPhrase::defaultLocale)
      sendMessage(identity, miniPhrase.translate(key, targetLocale, tags), type)
    }
  }
}

/**
 * Sends a message to this audience, with optional contextual tags.
 *
 * This method will only operate on this audience, or any child audience in the case that
 * this is a forwarding audience, if they are of type [T].
 *
 * The message will be translated using the provided [locale]. If this locale is null,
 * it will instead be translated using the locale provided from the pointers of the
 * audience. If this is not available, or they do not have a locale, the default locale
 * for the MiniPhrase instance in use will be used.
 *
 * This method will account for any forwarding audiences, automatically this method on
 * each child, as dictated by the [Audience.forEachAudience] method. This method may
 * optionally decide to act on itself, depending on the implementation of the method.
 */
context(MiniPhraseContext)
public inline fun <reified T : Audience> Audience.sendTranslatedContextual(
  /** The key of the message. */
  key: String,
  /** The type of the message. */
  type: MessageType = MessageType.CHAT,
  /** The identity of the message sender. */
  identity: Identity = Identity.nil(),
  /** The locale to translate the message in, if not the default for the audience. */
  locale: Locale? = null,
  /* A builder of additional tags to use in the deserialization process. */
  noinline tags: ContextualTagBuilder<T>
) {
  sendTranslatedContextual(T::class, key, type, identity, locale, tags)
}

/**
 * Sends a message to this audience, with optional contextual tags.
 *
 * This method will only operate on this audience, or any child audience in the case that
 * this is a forwarding audience, if they are of type [T].
 *
 * The message will be translated using the provided [locale]. If this locale is null,
 * it will instead be translated using the locale provided from the pointers of the
 * audience. If this is not available, or they do not have a locale, the default locale
 * for the MiniPhrase instance in use will be used.
 *
 * This method will account for any forwarding audiences, automatically this method on
 * each child, as dictated by the [Audience.forEachAudience] method. This method may
 * optionally decide to act on itself, depending on the implementation of the method.
 */
context(MiniPhraseContext)
public fun <T : Audience> Audience.sendTranslatedContextual(
  /** The type of the audience. */
  audienceType: KClass<T>,
  /** The key of the message. */
  key: String,
  /** The type of the message. */
  type: MessageType = MessageType.CHAT,
  /** The identity of the message sender. */
  identity: Identity = Identity.nil(),
  /** The locale to translate the message in, if not the default for the audience. */
  locale: Locale? = null,
  /* A builder of additional tags to use in the deserialization process. */
  tags: ContextualTagBuilder<T>
) {
  when {
    this === Audience.empty() -> {
      // Do nothing if the audience is the empty audience.
    }

    locale == null && this is ForwardingAudience -> {
      forEachAudience { child ->
        // If the audience has decided to iterate on itself, we prevent a stack overflow here
        // by skipping this case.
        if (child !== this) {
          child.sendTranslatedContextual(audienceType, key, type, identity, null, tags)
        }
      }
    }

    audienceType.isInstance(this) -> {
      // Only operate if this is of the correct type.
      audienceType.safeCast(this)?.let { audience ->
        sendMessage(identity, miniPhrase.translate(key, locale, tags.asNonContextual(audience)), type)
      }
    }
  }
}
