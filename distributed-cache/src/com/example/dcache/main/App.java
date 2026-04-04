package com.example.dcache.main;

import com.example.dcache.domain.CacheNode;
import com.example.dcache.domain.eviction.LruEvictionPolicy;
import com.example.dcache.domain.strategy.ModuloDistributionStrategy;
import com.example.dcache.repository.InMemoryKeyValueDatabase;
import com.example.dcache.repository.KeyValueDatabase;
import com.example.dcache.service.DistributedCacheService;
import java.util.ArrayList;
import java.util.List;

public class App {

  public static void main(String[] args) {
    KeyValueDatabase database = new InMemoryKeyValueDatabase();
    database.put("db-only", "from-db");

    int nodeCount = 3;
    int capacityPerNode = 2;
    LruEvictionPolicy evictionPolicy = new LruEvictionPolicy();
    List<CacheNode> nodes = new ArrayList<>();
    for (int i = 0; i < nodeCount; i++) {
      nodes.add(new CacheNode(i, evictionPolicy.createStore(capacityPerNode)));
    }

    DistributedCacheService cache =
        new DistributedCacheService(database, nodes, new ModuloDistributionStrategy());

    System.out.println("get miss then load: " + cache.get("db-only"));
    cache.put("a", "1");
    cache.put("b", "2");
    cache.put("c", "3");
    System.out.println("eviction with capacity " + capacityPerNode + " per node:");
    System.out.println("get a: " + cache.get("a"));
    System.out.println("get b: " + cache.get("b"));
    System.out.println("get c: " + cache.get("c"));
  }
}
