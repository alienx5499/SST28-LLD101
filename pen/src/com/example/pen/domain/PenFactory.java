package com.example.pen.domain;

public final class PenFactory {

  private PenFactory() {}

  public static Pen create(PenType penType, int maxInkCapacity, int initialInkLevel) {
    if (penType == PenType.FOUNTAIN) {
      return new FountainPen(maxInkCapacity, initialInkLevel);
    }
    if (penType == PenType.BALLPOINT) {
      return new BallPen(maxInkCapacity, initialInkLevel);
    }
    if (penType == PenType.GEL) {
      return new GelPen(maxInkCapacity, initialInkLevel);
    }
    throw new IllegalArgumentException("Unsupported pen type: " + penType);
  }
}
