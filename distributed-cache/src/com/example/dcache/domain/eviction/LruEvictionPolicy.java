package com.example.dcache.domain.eviction;

import com.example.dcache.domain.store.LocalCacheStore;
import com.example.dcache.domain.store.LruLocalCacheStore;

public final class LruEvictionPolicy implements EvictionPolicy {

  @Override
  public LocalCacheStore createStore(int capacityPerNode) {
    return new LruLocalCacheStore(capacityPerNode);
  }
}
