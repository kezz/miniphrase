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
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.audience.MessageType
import net.kyori.adventure.identity.Identity
import java.util.Locale
import kotlin.reflect.KClass

/** Shorthand for [Audience.audience]. */
public fun Array<out Audience>.asAudience(): Audience =
  Audience.audience(*this)

/** @see [Audience.sendTranslated]. */
context(MiniPhraseContext)
public fun Array<out Audience>.sendTranslated(
  key: String,
  type: MessageType = MessageType.CHAT,
  identity: Identity = Identity.nil(),
  locale: Locale? = null,
  tags: GenericTagBuilder? = null
) {
  if (locale != null) {
    // If we've got an override locale, we can save rendering by wrapping this in an audience.
    asAudience().sendTranslated(key, type, identity, locale, tags)
  } else {
    // Otherwise, we need to pull it from each audience member, so just delegate.
    forEach { audience -> audience.sendTranslated(key, type, identity, null, tags) }
  }
}

/** @see [Audience.sendTranslatedContextual] */
context(MiniPhraseContext)
public inline fun <reified T : Audience> Array<Audience>.sendTranslatedContextual(
  key: String,
  type: MessageType = MessageType.CHAT,
  identity: Identity = Identity.nil(),
  locale: Locale? = null,
  noinline tags: ContextualTagBuilder<T>
) {
  forEach { audience -> audience.sendTranslatedContextual(T::class, key, type, identity, locale, tags) }
}

/** @see [Audience.sendTranslatedContextual]. */
context(MiniPhraseContext)
public fun <T : Audience> Array<out Audience>.sendTranslatedContextual(
  audienceType: KClass<T>,
  key: String,
  type: MessageType = MessageType.CHAT,
  identity: Identity = Identity.nil(),
  locale: Locale? = null,
  tags: ContextualTagBuilder<T>
) {
  forEach { audience -> audience.sendTranslatedContextual(audienceType, key, type, identity, locale, tags) }
}
