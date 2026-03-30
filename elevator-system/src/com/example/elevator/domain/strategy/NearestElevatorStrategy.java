package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ExternalRequest;
import java.util.*;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy {

  @Override
  public Elevator selectElevator(ExternalRequest request, List<Elevator> availableElevators) {
    if (availableElevators.isEmpty()) {
      return null;
    }

    Elevator nearest = null;
    int minDistance = Integer.MAX_VALUE;

    for (Elevator elevator : availableElevators) {
      if (!elevator.isAvailable()) continue;

      int distance = Math.abs(elevator.getCurrentFloor() - request.getFloorNumber());

      if (distance < minDistance) {
        minDistance = distance;
        nearest = elevator;
      }
    }

    return nearest;
  }
}
