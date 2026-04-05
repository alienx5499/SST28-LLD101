package com.example.ratelimit.service;

import com.example.ratelimit.domain.RateLimitResult;

public final class ExternalCallCoordinator {

  private final ExternalResourceRateLimiter rateLimiter;

  public ExternalCallCoordinator(ExternalResourceRateLimiter rateLimiter) {
    this.rateLimiter = rateLimiter;
  }

  public boolean runWithOptionalExternalStep(
      boolean needsPaidExternalCall, String rateLimitKey, Runnable externalCall) {
    if (!needsPaidExternalCall) {
      return true;
    }
    RateLimitResult decision = rateLimiter.checkBeforeExternalCall(rateLimitKey);
    if (!decision.permitted()) {
      return false;
    }
    externalCall.run();
    return true;
  }
}
