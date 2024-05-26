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
import dev.kezz.miniphrase.tag.TagResolverBuilder
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.ForwardingAudience
import net.kyori.adventure.identity.Identity
import java.util.Locale

/**
 * Sends a message to this audience, with optional additional tags.
 *
 * The message will be translated using the provided [locale]. If this locale is null,
 * it will instead be translated using the locale provided from the pointers of the
 * audience. If this is not available, or they do not have a locale, the default locale
 * for the MiniPhrase instance in use will be used.
 *
 * This method will account for any forwarding audiences, automatically calling this
 * method again on their children, skipping the parent audience. If the locale that is
 * provided is not null, then this will be skipped as the resulting component is fixed
 * for all recipients of the message.
 */
context(MiniPhraseContext)
public fun Audience.sendTranslated(
  /** The key of the message. */
  key: String,
  /** The locale to translate the message in, if not the default for the audience. */
  locale: Locale? = null,
  // A builder of additional tags to use in the deserialization process.
  tags: (TagResolverBuilder.() -> Unit)? = null,
) {
  when {
    this == Audience.empty() -> {
      // Do nothing if this audience is the empty audience.
    }

    locale == null && this is ForwardingAudience -> {
      // We only run through each child if the locale is null (i.e. we're pulling it from the audience itself).
      forEachAudience { child -> child.sendTranslated(key, locale, tags) }
    }

    else -> {
      // Try and get the locale from the audience, otherwise default, then translate and send!
      val targetLocale = locale ?: get(Identity.LOCALE).orElseGet(miniPhrase::defaultLocale)
      sendMessage(miniPhrase.translate(key, targetLocale, tags))
    }
  }
}

context(MiniPhraseContext)
public fun Audience.sendTranslatedIfPresent(
  /** The key of the message. */
  key: String,
  /** The locale to translate the message in, if not the default for the audience. */
  locale: Locale? = null,
  // A builder of additional tags to use in the deserialization process.
  tags: (TagResolverBuilder.() -> Unit)? = null,
) {
  when {
    this == Audience.empty() -> {
      // Do nothing if this audience is the empty audience.
    }

    locale == null && this is ForwardingAudience -> {
      // We only run through each child if the locale is null (i.e. we're pulling it from the audience itself).
      forEachAudience { child -> child.sendTranslated(key, locale, tags) }
    }

    else -> {
      // Try and get the locale from the audience, otherwise default, then translate and send!
      val targetLocale = locale ?: get(Identity.LOCALE).orElseGet(miniPhrase::defaultLocale)
      miniPhrase.translateOrNull(key, targetLocale, tags)?.let { sendMessage(it) }
    }
  }
}
