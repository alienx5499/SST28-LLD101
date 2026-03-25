package com.example.snakeladder.domain;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public final class Board {

  private final int n;
  private final int lastCell;
  private final Map<Integer, Integer> snakes;
  private final Map<Integer, Integer> ladders;

  public Board(int n, Map<Integer, Integer> snakes, Map<Integer, Integer> ladders) {
    this.n = n;
    this.lastCell = n * n;
    this.snakes = Collections.unmodifiableMap(snakes);
    this.ladders = Collections.unmodifiableMap(ladders);
  }

  public int size() {
    return n;
  }

  public int lastCell() {
    return lastCell;
  }

  public Map<Integer, Integer> snakes() {
    return snakes;
  }

  public Map<Integer, Integer> ladders() {
    return ladders;
  }

  public Optional<Integer> snakeTail(int position) {
    return Optional.ofNullable(snakes.get(position));
  }

  public Optional<Integer> ladderEnd(int position) {
    return Optional.ofNullable(ladders.get(position));
  }
}
