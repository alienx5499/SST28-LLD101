# Snake and Ladder

## Explanation of the design and approach

### What this is

- A multi-player Snake and Ladder game with board size `n x n`, cells from `1` to `n^2`, and player pieces starting at `0`.
- The board is generated with exactly `n` snakes and `n` ladders using random placement.
- Snake heads map to smaller tails, ladder starts map to larger ends, and generator validation avoids cycles.

### Game flow

- Players play turn-by-turn in input order.
- A six-sided random dice gives values from `1` to `6`.
- If `current + dice > n^2`, the player does not move.
- If a move ends at a snake head, the player goes down to the snake tail.
- If a move ends at a ladder start, the player goes up to the ladder end.
- A player wins on reaching exactly `n^2`.
- The game continues while at least two players are still in contention.

### How the code is layered

- `main` reads input and wires board, players, dice, and game service.
- `domain` contains board entities (`Snake`, `Ladder`, `Player`, `Board`) and dice abstraction.
- `service` contains board generation and turn/game orchestration.

### Important design decisions

- `BoardGenerator` keeps snake and ladder placement random while enforcing no cycles across jump edges.
- `GameService` is responsible for rules only; it does not know how board elements are generated.
- `Dice` is an interface so deterministic test dice can replace `RandomDice` in tests.

## Class diagram

<img src="https://github.com/user-attachments/assets/38035489-81e9-4125-a4bb-11ab3474e894" alt="Snake and Ladder class diagram" width="100%" />

