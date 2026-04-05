# Pluggable rate limiting (external resource)

## Explanation of the design and approach

### What this is

- Rate limits apply **only when the system is about to call a paid external resource**, not on every inbound API request.
- Business code decides whether an external step is needed; only then `ExternalResourceRateLimiter` runs.
- Limits are expressed as **`RateLimitConfig(maxRequests, windowDuration)`**, for example **5 per minute** or **1000 per hour** by choosing `Duration.ofMinutes(1)` or `Duration.ofHours(1)`.
- The limit is keyed by a **string** built for the use case (`RateLimitKey` helpers for tenant, tenant+provider, raw key, API key id).

### Flow

- Client request enters your API and internal service runs business rules.
- If **no** external call is required, the rate limiter is **not** consulted.
- If an external call **is** required, the service calls `checkBeforeExternalCall(key)`; on **permit**, it performs the paid call; on **deny**, it can reject or degrade without charging quota for calls that never ran.

### Pluggable algorithms

- **`RateLimitAlgorithm`** is the extension point. **`FixedWindowCounterAlgorithm`** and **`SlidingWindowCounterAlgorithm`** implement it today.
- **`ExternalResourceRateLimiter`** depends only on `RateLimitAlgorithm` + `RateLimitConfig`, so you can swap implementations (token bucket, leaky bucket, sliding log) **without changing** `ExternalCallCoordinator` or domain services.
- **`ExternalCallCoordinator`** shows how internal code stays the same: it only needs a limiter instance; which algorithm backs it is wiring in `main`.

### Thread safety and testing

- Both algorithms use **`ConcurrentHashMap`** and **atomic per-key updates** so concurrent requests for different keys (or the same key) do not corrupt counts.
- **`RateLimitAlgorithm`** is a small interface, so tests can inject a fake or a spy. **`RateLimitResult`** is a plain record for assertions.

### Design decisions

- **Single window per limiter instance** keeps the API small; multiple policies (per minute and per hour) can be modeled with **two limiters** and **two keys**, or a future **composite** algorithm if you need one decision from both.
- **String keys** keep the module generic; callers own the naming scheme for tenant vs provider vs API key.
- **Sliding window** stores timestamps in a deque per key (bounded by `maxRequests` after pruning), trading a bit of memory for smoother fairness.

### Trade-offs: fixed window vs sliding window

| | Fixed window | Sliding window |
|---|----------------|----------------|
| **Behavior** | Count resets at each window boundary; simple counter per bucket. | Drops events older than the window; limit applies to a **rolling** time span. |
| **Burst** | A client can send many requests at the **start** of a new window (double burst across boundary). | Spreads load more evenly across time; **no** sharp reset spike at the boundary. |
| **Cost** | **O(1)** time and very small memory per key (one counter + window id). | **O(k)** per key in the worst case where *k* is the number of events still inside the window (capped by `maxRequests` after pruning). |
| **Pick when** | You want minimal state and accept boundary bursts. | You want stricter fairness over the last *N* minutes without arbitrary clock edges. |

## Class diagram

<img src="https://github.com/user-attachments/assets/9722e0a8-cefa-475b-98fd-291e9e89c01a" alt="Rate limiting class diagram" width="100%" />
