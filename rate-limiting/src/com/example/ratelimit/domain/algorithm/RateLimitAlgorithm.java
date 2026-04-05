package com.example.ratelimit.domain.algorithm;

import com.example.ratelimit.domain.RateLimitConfig;
import com.example.ratelimit.domain.RateLimitResult;

public interface RateLimitAlgorithm {

  RateLimitResult tryConsume(String key, RateLimitConfig config);
}
