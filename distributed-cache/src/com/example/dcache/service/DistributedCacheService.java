package com.example.dcache.service;

import com.example.dcache.domain.CacheNode;
import com.example.dcache.domain.strategy.DistributionStrategy;
import com.example.dcache.repository.KeyValueDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public final class DistributedCacheService {

  private final KeyValueDatabase database;
  private final List<CacheNode> nodes;
  private final DistributionStrategy distributionStrategy;

  public DistributedCacheService(
      KeyValueDatabase database, List<CacheNode> nodes, DistributionStrategy distributionStrategy) {
    if (nodes.isEmpty()) {
      throw new IllegalArgumentException("nodes must not be empty");
    }
    this.database = database;
    this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    this.distributionStrategy = distributionStrategy;
  }

  public String get(String key) {
    int index = distributionStrategy.nodeIndex(key, nodes.size());
    CacheNode node = nodes.get(index);
    Optional<String> cached = node.get(key);
    if (cached.isPresent()) {
      return cached.get();
    }
    Optional<String> loaded = database.get(key);
    if (loaded.isEmpty()) {
      throw new NoSuchElementException(key);
    }
    String value = loaded.get();
    node.put(key, value);
    return value;
  }

  public void put(String key, String value) {
    database.put(key, value);
    int index = distributionStrategy.nodeIndex(key, nodes.size());
    nodes.get(index).put(key, value);
  }

  public List<CacheNode> getNodes() {
    return nodes;
  }
}
