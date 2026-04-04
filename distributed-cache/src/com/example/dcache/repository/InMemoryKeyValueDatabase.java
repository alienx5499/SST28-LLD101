package com.example.dcache.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryKeyValueDatabase implements KeyValueDatabase {

  private final Map<String, String> data = new ConcurrentHashMap<>();

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(data.get(key));
  }

  @Override
  public void put(String key, String value) {
    data.put(key, value);
  }
}
