package com.experiments.customcache.cache;

import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

class CacheImpl<K extends Comparable<K>, V extends CacheableEntry> implements Cache<K, CompletableFuture<V>> {

  private final ConcurrentMap<K, CompletableFuture<ValueBox<V>>> memoryMap;
  private final Deque<K> keysMemory;
  private final Function<K, CompletableFuture<V>> loader;
  private final int sizeLimit;

  private final int ttlMillis;

  public CacheImpl(int sizeLimit, int ttlMillis, Function<K, CompletableFuture<V>> loader) {
    this.memoryMap = new ConcurrentHashMap<>(sizeLimit);
    this.keysMemory = new ConcurrentLinkedDeque<>();
    this.sizeLimit = sizeLimit;
    this.loader = loader;
    this.ttlMillis = ttlMillis;
  }

  @Override
  public CompletableFuture<V> get(K key) {
    final var valueBox = this.memoryMap.get(key);
    if (valueBox == null) {
      return this.fromResource(key, null);
    }
    return valueBox.thenCompose(vb -> {
      final var storedAt = vb.storedAt();
      final var diff = System.currentTimeMillis() - storedAt;
      if (diff > this.ttlMillis) {
        return fromResource(key, valueBox);
      }
      return CompletableFuture.supplyAsync(vb::value);
    });
  }

  private CompletableFuture<V> fromResource(K key, CompletableFuture<ValueBox<V>> oldValue) {
    return this.loader.apply(key)
        .thenCompose(v -> {
          final var vbox = new ValueBox<>(v, System.currentTimeMillis());
          if (v.allowedToCache()) {
            this.memorize(key, CompletableFuture.completedFuture(vbox));
          } else {
            if (oldValue != null) {
              return oldValue;
            }
          }
          return CompletableFuture.supplyAsync(() -> vbox);
        }).thenApply(ValueBox::value);
  }

  private void memorize(K key, CompletableFuture<ValueBox<V>> result) {
    if (this.memoryMap.size() > this.sizeLimit) {
      final var keyToRemove = keysMemory.removeFirst();
      this.memoryMap.remove(keyToRemove);
    }
    keysMemory.addLast(key);
    this.memoryMap.put(key, result);
  }
}
