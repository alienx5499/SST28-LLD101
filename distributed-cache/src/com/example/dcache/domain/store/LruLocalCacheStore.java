package com.example.dcache.domain.store;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class LruLocalCacheStore implements LocalCacheStore {

  private final Map<String, String> entries;

  public LruLocalCacheStore(int capacity) {
    final int max = capacity;
    this.entries =
        new LinkedHashMap<String, String>(16, 0.75f, true) {
          @Override
          protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > max;
          }
        };
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(entries.get(key));
  }

  @Override
  public void put(String key, String value) {
    entries.put(key, value);
  }
}
