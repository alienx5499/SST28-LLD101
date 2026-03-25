package com.example.snakeladder.service;

import com.example.snakeladder.domain.Board;
import com.example.snakeladder.domain.Dice;
import com.example.snakeladder.domain.Player;
import java.util.ArrayList;
import java.util.List;

public final class GameService {

  private final Board board;
  private final Dice dice;
  private final List<Player> players;

  public GameService(Board board, Dice dice, List<Player> players) {
    if (players.size() < 2) {
      throw new IllegalArgumentException("minimum 2 players required");
    }
    this.board = board;
    this.dice = dice;
    this.players = new ArrayList<>(players);
  }

  public void start() {
    int turn = 1;
    List<Player> winners = new ArrayList<>();

    while (activePlayersCount() >= 2) {
      for (Player player : players) {
        if (player.hasWon()) {
          continue;
        }
        playTurn(player);
        if (player.getPosition() == board.lastCell()) {
          player.markWon();
          winners.add(player);
          System.out.println(player.getName() + " wins at rank " + winners.size());
          if (activePlayersCount() < 2) {
            break;
          }
        }
      }
      turn++;
      if (turn > 100000) {
        throw new IllegalStateException("game did not finish");
      }
    }

    System.out.println("Game over");
    if (!winners.isEmpty()) {
      System.out.println("Winners in order:");
      for (int i = 0; i < winners.size(); i++) {
        System.out.println((i + 1) + ". " + winners.get(i).getName());
      }
    }

    for (Player player : players) {
      if (!player.hasWon()) {
        System.out.println("Still playing: " + player.getName() + " at " + player.getPosition());
      }
    }
  }

  private void playTurn(Player player) {
    int rolled = dice.roll();
    int from = player.getPosition();
    int tentative = from + rolled;

    if (tentative > board.lastCell()) {
      System.out.println(player.getName() + " rolled " + rolled + " from " + from + " and stays");
      return;
    }

    int resolved = tentative;
    Integer snakeTail = board.snakes().get(tentative);
    if (snakeTail != null) {
      resolved = snakeTail;
      System.out.println(
          player.getName()
              + " rolled "
              + rolled
              + ", moved to "
              + tentative
              + " and got bitten by snake to "
              + resolved);
    } else {
      Integer ladderEnd = board.ladders().get(tentative);
      if (ladderEnd != null) {
        resolved = ladderEnd;
        System.out.println(
            player.getName()
                + " rolled "
                + rolled
                + ", moved to "
                + tentative
                + " and climbed ladder to "
                + resolved);
      } else {
        System.out.println(player.getName() + " rolled " + rolled + " and moved to " + resolved);
      }
    }

    player.setPosition(resolved);
  }

  private long activePlayersCount() {
    return players.stream().filter(p -> !p.hasWon()).count();
  }
}
