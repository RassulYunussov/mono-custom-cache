package com.experiments.customcache.cache;

public interface CacheableEntry {
  default boolean allowedToCache() { return true;}
}
