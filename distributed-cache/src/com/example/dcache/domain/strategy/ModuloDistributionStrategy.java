package com.example.dcache.domain.strategy;

public final class ModuloDistributionStrategy implements DistributionStrategy {

  @Override
  public int nodeIndex(String key, int nodeCount) {
    if (nodeCount <= 0) {
      throw new IllegalArgumentException("nodeCount must be positive");
    }
    return Math.floorMod(key.hashCode(), nodeCount);
  }
}
