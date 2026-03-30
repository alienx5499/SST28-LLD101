package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Direction;
import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.InternalRequest;
import java.util.*;
import java.util.stream.*;

public class ScanStrategy implements MovementStrategy {

  @Override
  public List<Integer> calculatePath(Elevator elevator, List<InternalRequest> requests) {
    List<Integer> path = new ArrayList<>();

    List<Integer> floors =
        requests.stream()
            .map(InternalRequest::getDestinationFloor)
            .distinct()
            .sorted()
            .collect(Collectors.toList());

    if (floors.isEmpty()) return path;

    int currentFloor = elevator.getCurrentFloor();
    Direction direction = elevator.getDirection();

    if (direction == Direction.IDLE) {
      direction = floors.get(0) > currentFloor ? Direction.UP : Direction.DOWN;
    }

    if (direction == Direction.UP) {
      floors.stream().filter(f -> f >= currentFloor).forEach(path::add);
      floors.stream().filter(f -> f < currentFloor).sorted((a, b) -> b - a).forEach(path::add);
    } else {
      floors.stream().filter(f -> f <= currentFloor).sorted((a, b) -> b - a).forEach(path::add);
      floors.stream().filter(f -> f > currentFloor).forEach(path::add);
    }

    return path;
  }
}
