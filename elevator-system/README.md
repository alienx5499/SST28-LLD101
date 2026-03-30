# Explanation and Design Approach

This project is designed using layered architecture so that each part has one clear role.

- `controller` layer takes button actions and API style calls.
- `service` layer contains core elevator logic (dispatch, movement, maintenance, emergency handling).
- `repository` layer stores data in memory.
- `domain` layer contains models, enums, and strategy interfaces.

Main flow:
1. Outside button creates an external request.
2. Dispatcher selects one elevator using strategy.
3. Request is converted to internal request for that elevator.
4. Movement service processes pending requests and updates floor/state.
5. Elevator opens/closes doors when destination is reached.

Important design decisions:
- Strategy pattern is used for elevator selection (`Nearest`, `LoadBalancing`) so new algorithms can be added easily.
- State pattern is used for elevator behavior (`Stopped`, `Moving`, `DoorsOpening`, `DoorsClosing`, `Maintenance`, `PreMaintenance`).
- Repository abstraction keeps storage logic separate from business logic.
- Priority queue is used for request prioritization.
- Safety rules are handled in services (weight limit, emergency stop, maintenance mode).

---

## Class diagram

<img src="https://github.com/user-attachments/assets/4db7506b-3e3b-4fce-b5c5-75c9a7bcdbfb" alt="Class diagram" width="100%" />