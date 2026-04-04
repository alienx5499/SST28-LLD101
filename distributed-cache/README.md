# Distributed cache

## Explanation of the design and approach

### What this is

- A distributed cache split across multiple **cache nodes** (count and per-node capacity are configurable in `App`).
- Everything is **in memory**; there is no real network between nodes—this stays an LLD exercise focused on routing, misses, and eviction.
- The **database** is only an interface with an in-memory implementation; you can swap a real DB later without changing the service.

### Get flow

- A key is routed to exactly one node using a **distribution strategy** (default: hash-based modulo on `nodeCount`).
- If the value is in that node’s store, it is returned.
- On a **cache miss**, the value is read from the database, written into that node, then returned. If the key is not in the database, `get` fails (no value to load).

### Put flow

- **Assumption:** the backing database is updated first, then the value is written into the node chosen for that key (same routing rule as `get`). That keeps the DB authoritative and the cache as a replica.

### Distribution and eviction

- **Distribution** is behind `DistributionStrategy` so you can replace modulo with something like consistent hashing later without touching `DistributedCacheService`.
- Each node uses an **eviction policy** to build its `LocalCacheStore`. The default is **LRU** via `LruLocalCacheStore` (access-ordered map with a max size per node). New policies can return different stores (for example LFU or MRU) through the same `EvictionPolicy` hook.

### How the code is layered

- `main` wires node count, capacities, strategies, and the database.
- `DistributedCacheService` owns `get` / `put`, talks to the DB on miss or put, and delegates storage to the right `CacheNode`.
- `CacheNode` wraps a `LocalCacheStore` (shard).
- `repository` holds the database abstraction; `domain` holds `CacheNode`, routing, eviction, and store interfaces.

## Class diagram

<img src="https://github.com/user-attachments/assets/065a1c6c-e63e-4807-866e-bfbedbe15023" alt="Distributed cache class diagram" width="100%" />

