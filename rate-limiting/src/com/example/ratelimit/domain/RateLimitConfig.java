package com.example.ratelimit.domain;

import java.time.Duration;
import java.util.Objects;

public record RateLimitConfig(int maxRequests, Duration windowDuration) {
  public RateLimitConfig {
    if (maxRequests <= 0) {
      throw new IllegalArgumentException("maxRequests must be positive");
    }
    Objects.requireNonNull(windowDuration, "windowDuration");
    if (windowDuration.isNegative() || windowDuration.isZero()) {
      throw new IllegalArgumentException("windowDuration must be positive");
    }
  }
}
