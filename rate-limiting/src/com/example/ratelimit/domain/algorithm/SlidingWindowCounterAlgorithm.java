package com.example.ratelimit.domain.algorithm;

import com.example.ratelimit.domain.RateLimitConfig;
import com.example.ratelimit.domain.RateLimitResult;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SlidingWindowCounterAlgorithm implements RateLimitAlgorithm {

  private final ConcurrentHashMap<String, Deque<Long>> events = new ConcurrentHashMap<>();

  @Override
  public RateLimitResult tryConsume(String key, RateLimitConfig config) {
    long now = System.currentTimeMillis();
    long windowMillis = config.windowDuration().toMillis();
    long cutoff = now - windowMillis;
    int max = config.maxRequests();
    AtomicBoolean allowed = new AtomicBoolean(false);
    events.compute(
        key,
        (k, previous) -> {
          Deque<Long> deque =
              previous == null ? new ArrayDeque<>() : new ArrayDeque<>(new ArrayList<>(previous));
          while (!deque.isEmpty() && deque.peekFirst() < cutoff) {
            deque.pollFirst();
          }
          if (deque.size() >= max) {
            allowed.set(false);
            return deque;
          }
          allowed.set(true);
          deque.addLast(now);
          return deque;
        });
    return allowed.get()
        ? RateLimitResult.allowed()
        : RateLimitResult.denied("sliding window full");
  }
}
