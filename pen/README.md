# Pen (LLD Design + Implementation)

## Design

- `Pen` defines the behavior contract: `start`, `write`, `close`, `refill`.
- `CoreInkPen` centralizes lifecycle + shared writing rules.
- `InkTank` owns ink state and refill/draw logic.
- `FountainPen`, `BallPen`, and `GelPen` only define type-specific ink behavior.
- `PenFactory` creates concrete `Pen` implementations so app code depends on abstractions.
- `Paper` abstracts writing target.
- `SimplePaper` is an in-memory paper implementation.

## Rule mapping

- `start()` enables writing and requires ink (`inkLevel > 0`).
- `write(text)` works only when started and enough ink exists.
- `close()` disables writing.
- `refill(units)` adds ink up to max capacity.
- Ink usage differs by pen type (`FountainPen` consumes more ink per character than `BallPen`/`GelPen`).

## Class diagram

<img src="https://github.com/user-attachments/assets/9bd179dc-78ce-43b9-a407-d3ddac8c5718" alt="Pen class diagram" width="100%" />