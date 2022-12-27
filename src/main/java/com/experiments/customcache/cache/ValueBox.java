package com.experiments.customcache.cache;

public  record ValueBox <V> (V value, Long storedAt) {
}
