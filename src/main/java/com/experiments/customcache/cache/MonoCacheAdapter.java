package com.experiments.customcache.cache;

import java.util.function.Function;
import reactor.core.publisher.Mono;

public class MonoCacheAdapter<K extends Comparable<K>,V extends CacheableEntry> implements Cache<K,Mono<V>>{
  final CacheImpl<K,V> cache;

  public MonoCacheAdapter(int size, int ttlMillis, Function<K, Mono<V>> loader) {
    this.cache = new CacheImpl<K,V>(size,ttlMillis, k->loader.apply(k).toFuture());
  }

  @Override
  public Mono<V> get(K key) {
    return Mono.fromCompletionStage(this.cache.get(key));
  }
}
