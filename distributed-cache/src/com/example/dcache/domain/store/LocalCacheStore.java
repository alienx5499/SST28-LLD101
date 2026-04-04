package com.example.dcache.domain.store;

import java.util.Optional;

public interface LocalCacheStore {

  Optional<String> get(String key);

  void put(String key, String value);
}
