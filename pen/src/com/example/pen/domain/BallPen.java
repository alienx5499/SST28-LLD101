package com.example.pen.domain;

public final class BallPen extends CoreInkPen {

  public BallPen(int maxInkCapacity, int initialInkLevel) {
    super(maxInkCapacity, initialInkLevel);
  }

  @Override
  protected int inkUnitsFor(String text) {
    return text.length();
  }
}
