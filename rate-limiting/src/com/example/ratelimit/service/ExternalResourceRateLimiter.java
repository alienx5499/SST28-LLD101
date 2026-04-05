package com.example.ratelimit.service;

import com.example.ratelimit.domain.RateLimitConfig;
import com.example.ratelimit.domain.RateLimitResult;
import com.example.ratelimit.domain.algorithm.RateLimitAlgorithm;

public final class ExternalResourceRateLimiter {

  private final RateLimitAlgorithm algorithm;
  private final RateLimitConfig config;

  public ExternalResourceRateLimiter(RateLimitAlgorithm algorithm, RateLimitConfig config) {
    this.algorithm = algorithm;
    this.config = config;
  }

  public RateLimitResult checkBeforeExternalCall(String rateLimitKey) {
    return algorithm.tryConsume(rateLimitKey, config);
  }

  public RateLimitAlgorithm algorithm() {
    return algorithm;
  }

  public RateLimitConfig config() {
    return config;
  }
}
