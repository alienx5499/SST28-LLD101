package com.example.pen.main;

import com.example.pen.domain.Paper;
import com.example.pen.domain.Pen;
import com.example.pen.domain.PenFactory;
import com.example.pen.domain.PenType;
import com.example.pen.domain.SimplePaper;

public class App {

  public static void main(String[] args) {
    Pen pen = PenFactory.create(PenType.GEL, 30, 10);
    Paper paper = new SimplePaper();

    pen.start();
    pen.write("class PenDemo {", paper);
    pen.close();

    pen.refill(10);
    pen.start();
    pen.write(" void run() {} }", paper);
    pen.close();

    System.out.println("Final paper content: " + paper.read());
  }
}
