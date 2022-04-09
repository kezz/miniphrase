package dev.kezz.miniphrase;

import org.jetbrains.annotations.NotNull;

/** An object with an associated MiniPhrase instance. */
public interface MiniPhraseContext {
  /** The MiniPhrase instance associated with this object. */
  @NotNull MiniPhrase getMiniPhrase();
}
