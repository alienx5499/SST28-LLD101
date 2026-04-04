package com.example.dcache.domain.strategy;

public interface DistributionStrategy {

  int nodeIndex(String key, int nodeCount);
}
