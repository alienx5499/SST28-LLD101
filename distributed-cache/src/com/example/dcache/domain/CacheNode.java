package com.example.dcache.domain;

import com.example.dcache.domain.store.LocalCacheStore;
import java.util.Optional;

public final class CacheNode {

  private final int id;
  private final LocalCacheStore store;

  public CacheNode(int id, LocalCacheStore store) {
    this.id = id;
    this.store = store;
  }

  public int getId() {
    return id;
  }

  public Optional<String> get(String key) {
    return store.get(key);
  }

  public void put(String key, String value) {
    store.put(key, value);
  }
}
