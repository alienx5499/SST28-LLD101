package com.example.ratelimit.domain.algorithm;

import com.example.ratelimit.domain.RateLimitConfig;
import com.example.ratelimit.domain.RateLimitResult;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FixedWindowCounterAlgorithm implements RateLimitAlgorithm {

  private final ConcurrentHashMap<String, WindowState> slots = new ConcurrentHashMap<>();

  private static final class WindowState {
    private final long windowId;
    private final int count;

    private WindowState(long windowId, int count) {
      this.windowId = windowId;
      this.count = count;
    }
  }

  @Override
  public RateLimitResult tryConsume(String key, RateLimitConfig config) {
    long now = System.currentTimeMillis();
    long windowMillis = config.windowDuration().toMillis();
    long windowId = Math.floorDiv(now, windowMillis);
    AtomicBoolean allowed = new AtomicBoolean(false);
    slots.compute(
        key,
        (k, previous) -> {
          if (previous == null || previous.windowId != windowId) {
            allowed.set(true);
            return new WindowState(windowId, 1);
          }
          if (previous.count >= config.maxRequests()) {
            allowed.set(false);
            return previous;
          }
          allowed.set(true);
          return new WindowState(windowId, previous.count + 1);
        });
    return allowed.get() ? RateLimitResult.allowed() : RateLimitResult.denied("fixed window full");
  }
}
