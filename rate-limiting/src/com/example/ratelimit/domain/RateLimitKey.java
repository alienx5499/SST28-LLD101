package com.example.ratelimit.domain;

import java.util.Objects;

public final class RateLimitKey {

  private final String value;

  private RateLimitKey(String value) {
    this.value = Objects.requireNonNull(value, "value");
  }

  public static RateLimitKey of(String rawKey) {
    return new RateLimitKey(rawKey);
  }

  public static RateLimitKey tenant(String tenantId) {
    return new RateLimitKey("tenant:" + tenantId);
  }

  public static RateLimitKey tenantAndProvider(String tenantId, String providerId) {
    return new RateLimitKey("tenant:" + tenantId + "|provider:" + providerId);
  }

  public static RateLimitKey apiKey(String apiKeyId) {
    return new RateLimitKey("apiKey:" + apiKeyId);
  }

  public String value() {
    return value;
  }
}
