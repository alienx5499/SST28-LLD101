package com.example.snakeladder.domain;

public final class Player {

  private final String name;
  private int position;
  private boolean won;

  public Player(String name) {
    this.name = name;
    this.position = 0;
    this.won = false;
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public boolean hasWon() {
    return won;
  }

  public void markWon() {
    this.won = true;
  }
}
