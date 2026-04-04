package com.example.dcache.repository;

import java.util.Optional;

public interface KeyValueDatabase {

  Optional<String> get(String key);

  void put(String key, String value);
}
