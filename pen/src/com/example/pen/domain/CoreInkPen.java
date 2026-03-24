package com.example.pen.domain;

public abstract class CoreInkPen implements Pen {

  private final InkTank tank;
  private boolean started;

  protected CoreInkPen(int maxInkCapacity, int initialInkLevel) {
    this.tank = new InkTank(maxInkCapacity, initialInkLevel);
    this.started = false;
  }

  @Override
  public void start() {
    if (tank.isEmpty()) {
      throw new IllegalStateException("Cannot start: no ink available");
    }
    started = true;
  }

  @Override
  public void write(String text, Paper paper) {
    if (!started) {
      throw new IllegalStateException("Cannot write: pen is not started");
    }
    if (text == null || text.isEmpty()) {
      return;
    }
    int requiredInk = inkUnitsFor(text);
    if (!tank.hasAtLeast(requiredInk)) {
      throw new IllegalStateException("Cannot write: not enough ink");
    }
    paper.write(text);
    tank.draw(requiredInk);
  }

  @Override
  public void close() {
    started = false;
  }

  @Override
  public void refill(int units) {
    tank.refill(units);
  }

  public int getInkLevel() {
    return tank.current();
  }

  protected abstract int inkUnitsFor(String text);
}
