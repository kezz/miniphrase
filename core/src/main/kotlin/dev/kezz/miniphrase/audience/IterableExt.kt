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
import java.util.Locale

/** Shorthand for [Audience.audience]. */
public fun Iterable<Audience>.asAudience(): Audience = Audience.audience(this)

/** @see [Audience.sendTranslated] */
context(MiniPhraseContext)
public fun Iterable<Audience>.sendTranslated(
  key: String,
  locale: Locale? = null,
  tags: (TagResolverBuilder.() -> Unit)? = null,
) {
  if (locale != null) {
    // If we've got an override locale, we can save rendering by wrapping this in an audience.
    asAudience().sendTranslated(key, locale, tags)
  } else {
    // Otherwise, we need to pull it from each audience member, so just delegate.
    forEach { audience -> audience.sendTranslated(key, locale, tags) }
  }
}
