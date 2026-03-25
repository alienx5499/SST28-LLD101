package com.example.snakeladder.domain;

import java.util.Random;

public final class RandomDice implements Dice {

  private final Random random;

  public RandomDice() {
    this.random = new Random();
  }

  public RandomDice(long seed) {
    this.random = new Random(seed);
  }

  @Override
  public int roll() {
    return random.nextInt(6) + 1;
  }
}
