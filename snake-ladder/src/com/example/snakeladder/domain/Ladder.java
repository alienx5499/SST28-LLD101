package com.example.snakeladder.domain;

public record Ladder(int start, int end) {
  public Ladder {
    if (end <= start) {
      throw new IllegalArgumentException("ladder end must be greater than start");
    }
  }
}
