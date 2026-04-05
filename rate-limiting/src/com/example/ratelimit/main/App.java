package com.example.ratelimit.main;

import com.example.ratelimit.domain.RateLimitConfig;
import com.example.ratelimit.domain.RateLimitKey;
import com.example.ratelimit.domain.algorithm.FixedWindowCounterAlgorithm;
import com.example.ratelimit.domain.algorithm.RateLimitAlgorithm;
import com.example.ratelimit.domain.algorithm.SlidingWindowCounterAlgorithm;
import com.example.ratelimit.service.ExternalCallCoordinator;
import com.example.ratelimit.service.ExternalResourceRateLimiter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class App {

  public static void main(String[] args) {
    RateLimitConfig perMinute = new RateLimitConfig(5, Duration.of(1, ChronoUnit.MINUTES));
    String key = RateLimitKey.tenant("T1").value();

    System.out.println(
        "=== Fixed window: 5 external calls per minute (7 attempts, all need external) ===");
    demoAllExternal(new FixedWindowCounterAlgorithm(), perMinute, key);

    System.out.println("=== Sliding window: same limit ===");
    demoAllExternal(new SlidingWindowCounterAlgorithm(), perMinute, key);

    System.out.println("=== Mixed: external only when needed (no quota on other requests) ===");
    demoMixed(new FixedWindowCounterAlgorithm(), perMinute, key);
  }

  private static void demoAllExternal(
      RateLimitAlgorithm algorithm, RateLimitConfig config, String key) {
    ExternalResourceRateLimiter limiter = new ExternalResourceRateLimiter(algorithm, config);
    ExternalCallCoordinator coordinator = new ExternalCallCoordinator(limiter);
    for (int i = 1; i <= 7; i++) {
      final int callId = i;
      boolean ok =
          coordinator.runWithOptionalExternalStep(
              true,
              key,
              () -> System.out.println("  attempt " + callId + ": external paid API invoked"));
      if (!ok) {
        System.out.println("  attempt " + callId + ": blocked (rate limit)");
      }
    }
  }

  private static void demoMixed(RateLimitAlgorithm algorithm, RateLimitConfig config, String key) {
    ExternalResourceRateLimiter limiter = new ExternalResourceRateLimiter(algorithm, config);
    ExternalCallCoordinator coordinator = new ExternalCallCoordinator(limiter);
    for (int i = 1; i <= 4; i++) {
      final int callId = i;
      boolean needsExternal = i >= 3;
      boolean ok =
          coordinator.runWithOptionalExternalStep(
              needsExternal,
              key,
              () -> System.out.println("  call " + callId + ": external paid API invoked"));
      if (needsExternal && !ok) {
        System.out.println("  call " + callId + ": blocked");
      }
      if (!needsExternal) {
        System.out.println("  call " + callId + ": no external call, no quota");
      }
    }
  }
}
