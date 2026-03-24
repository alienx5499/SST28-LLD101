package com.example.pen.domain;

public interface Pen {

  void start();

  void write(String text, Paper paper);

  void close();

  void refill(int units);
}
