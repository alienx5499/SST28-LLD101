package com.example.ratelimit.domain;

import java.util.Optional;

public record RateLimitResult(boolean permitted, Optional<String> reason) {

  public static RateLimitResult allowed() {
    return new RateLimitResult(true, Optional.empty());
  }

  public static RateLimitResult denied(String reason) {
    return new RateLimitResult(false, Optional.ofNullable(reason));
  }
}
