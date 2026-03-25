package com.example.snakeladder.service;

import com.example.snakeladder.domain.Board;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public final class BoardGenerator {

  private final Random random;

  public BoardGenerator() {
    this.random = new Random();
  }

  public BoardGenerator(long seed) {
    this.random = new Random(seed);
  }

  public Board generate(int n) {
    if (n <= 1) {
      throw new IllegalArgumentException("n must be greater than 1");
    }
    int lastCell = n * n;
    if (lastCell - 2 < 4 * n) {
      throw new IllegalArgumentException("board too small for n snakes and n ladders");
    }

    Map<Integer, Integer> snakes = new HashMap<>();
    Map<Integer, Integer> ladders = new HashMap<>();
    Map<Integer, Integer> jumps = new HashMap<>();
    Set<Integer> usedCells = new HashSet<>();

    placeSnakes(n, lastCell, snakes, jumps, usedCells);
    placeLadders(n, lastCell, ladders, jumps, usedCells);

    return new Board(n, snakes, ladders);
  }

  private void placeSnakes(
      int count,
      int lastCell,
      Map<Integer, Integer> snakes,
      Map<Integer, Integer> jumps,
      Set<Integer> usedCells) {
    int placed = 0;
    int attempts = 0;
    int maxAttempts = 200000;

    while (placed < count && attempts < maxAttempts) {
      attempts++;
      int head = randomCell(2, lastCell - 1);
      int tail = randomCell(1, lastCell - 2);
      if (tail >= head) {
        continue;
      }
      if (usedCells.contains(head) || usedCells.contains(tail)) {
        continue;
      }
      if (createsCycle(head, tail, jumps)) {
        continue;
      }
      snakes.put(head, tail);
      jumps.put(head, tail);
      usedCells.add(head);
      usedCells.add(tail);
      placed++;
    }

    if (placed < count) {
      throw new IllegalStateException("could not place all snakes");
    }
  }

  private void placeLadders(
      int count,
      int lastCell,
      Map<Integer, Integer> ladders,
      Map<Integer, Integer> jumps,
      Set<Integer> usedCells) {
    int placed = 0;
    int attempts = 0;
    int maxAttempts = 200000;

    while (placed < count && attempts < maxAttempts) {
      attempts++;
      int start = randomCell(2, lastCell - 1);
      int end = randomCell(2, lastCell - 1);
      if (end <= start) {
        continue;
      }
      if (usedCells.contains(start) || usedCells.contains(end)) {
        continue;
      }
      if (createsCycle(start, end, jumps)) {
        continue;
      }
      ladders.put(start, end);
      jumps.put(start, end);
      usedCells.add(start);
      usedCells.add(end);
      placed++;
    }

    if (placed < count) {
      throw new IllegalStateException("could not place all ladders");
    }
  }

  private int randomCell(int lowInclusive, int highInclusive) {
    return random.nextInt(highInclusive - lowInclusive + 1) + lowInclusive;
  }

  private boolean createsCycle(int from, int to, Map<Integer, Integer> jumps) {
    return canReach(to, from, jumps);
  }

  private boolean canReach(int current, int target, Map<Integer, Integer> jumps) {
    Set<Integer> seen = new HashSet<>();
    int node = current;
    while (node != 0 && seen.add(node)) {
      if (node == target) {
        return true;
      }
      Integer next = jumps.get(node);
      if (next == null) {
        return false;
      }
      node = next;
    }
    return false;
  }
}
