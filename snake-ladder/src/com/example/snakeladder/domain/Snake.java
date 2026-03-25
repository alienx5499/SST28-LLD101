package com.example.snakeladder.domain;

public record Snake(int head, int tail) {
  public Snake {
    if (tail >= head) {
      throw new IllegalArgumentException("snake tail must be smaller than head");
    }
  }
}
