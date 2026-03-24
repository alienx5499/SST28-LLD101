package com.example.pen.domain;

public final class FountainPen extends CoreInkPen {

  public FountainPen(int maxInkCapacity, int initialInkLevel) {
    super(maxInkCapacity, initialInkLevel);
  }

  @Override
  protected int inkUnitsFor(String text) {
    return text.length() * 2;
  }
}
