package com.example.pen.domain;

public final class SimplePaper implements Paper {

  private final StringBuilder content;

  public SimplePaper() {
    this.content = new StringBuilder();
  }

  @Override
  public void write(String text) {
    content.append(text);
  }

  @Override
  public String read() {
    return content.toString();
  }
}
