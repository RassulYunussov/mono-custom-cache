package com.experiments.customcache.cache;

import java.util.concurrent.ExecutionException;

public interface Cache<K,V> {
 V get(K key) throws ExecutionException, InterruptedException;
}
