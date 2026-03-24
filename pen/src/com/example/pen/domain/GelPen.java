package com.example.pen.domain;

public final class GelPen extends CoreInkPen {

  public GelPen(int maxInkCapacity, int initialInkLevel) {
    super(maxInkCapacity, initialInkLevel);
  }

  @Override
  protected int inkUnitsFor(String text) {
    return text.length();
  }
}
