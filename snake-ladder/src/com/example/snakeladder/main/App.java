package com.example.snakeladder.main;

import com.example.snakeladder.domain.Board;
import com.example.snakeladder.domain.Dice;
import com.example.snakeladder.domain.Player;
import com.example.snakeladder.domain.RandomDice;
import com.example.snakeladder.service.BoardGenerator;
import com.example.snakeladder.service.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter board dimension n: ");
    int n = scanner.nextInt();

    System.out.print("Enter number of players: ");
    int playerCount = scanner.nextInt();
    scanner.nextLine();

    List<Player> players = new ArrayList<>();
    for (int i = 1; i <= playerCount; i++) {
      System.out.print("Enter player " + i + " name: ");
      String name = scanner.nextLine().trim();
      if (name.isEmpty()) {
        name = "P" + i;
      }
      players.add(new Player(name));
    }

    Board board = new BoardGenerator().generate(n);
    Dice dice = new RandomDice();

    printBoardSetup(board);
    System.out.println("Starting game with last cell = " + board.lastCell());

    GameService gameService = new GameService(board, dice, players);
    gameService.start();
  }

  private static void printBoardSetup(Board board) {
    System.out.println("Snakes (head -> tail):");
    for (Map.Entry<Integer, Integer> entry : board.snakes().entrySet()) {
      System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
    }

    System.out.println("Ladders (start -> end):");
    for (Map.Entry<Integer, Integer> entry : board.ladders().entrySet()) {
      System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
    }
  }
}
