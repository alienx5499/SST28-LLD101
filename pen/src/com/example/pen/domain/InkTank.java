package com.example.pen.domain;

public final class InkTank {

  private final int capacity;
  private int current;

  public InkTank(int capacity, int initialInk) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be positive");
    }
    if (initialInk < 0 || initialInk > capacity) {
      throw new IllegalArgumentException("initialInk must be between 0 and capacity");
    }
    this.capacity = capacity;
    this.current = initialInk;
  }

  public boolean isEmpty() {
    return current <= 0;
  }

  public boolean hasAtLeast(int units) {
    return current >= units;
  }

  public void draw(int units) {
    if (units < 0) {
      throw new IllegalArgumentException("units cannot be negative");
    }
    if (current < units) {
      throw new IllegalStateException("Cannot write: not enough ink");
    }
    current -= units;
  }

  public void refill(int units) {
    if (units <= 0) {
      throw new IllegalArgumentException("refill units must be positive");
    }
    current = Math.min(capacity, current + units);
  }

  public int current() {
    return current;
  }
}
