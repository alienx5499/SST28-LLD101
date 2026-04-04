package com.example.dcache.domain.eviction;

import com.example.dcache.domain.store.LocalCacheStore;

public interface EvictionPolicy {

  LocalCacheStore createStore(int capacityPerNode);
}
