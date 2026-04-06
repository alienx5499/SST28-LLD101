package com.example.map;

import java.util.HashMap;
import java.util.Map;

/**
 * DONE (student): Implement Flyweight factory that caches MarkerStyle by a stable key.
 *
 * <p>Suggested key format: shape + "|" + color + "|" + size + "|" + (filled ? "F" : "O")
 *
 * <p>After refactor: - MapDataSource should call this factory to obtain shared MarkerStyle
 * instances.
 */
public class MarkerStyleFactory {

  private final Map<String, MarkerStyle> cache = new HashMap<>();

  public MarkerStyle get(String shape, String color, int size, boolean filled) {
    String key = shape + "|" + color + "|" + size + "|" + (filled ? "F" : "O");
    MarkerStyle style = cache.get(key);
    if (style == null) {
      style = new MarkerStyle(shape, color, size, filled);
      cache.put(key, style);
    }
    return style;
  }

  public int cacheSize() {
    return cache.size();
  }
}
