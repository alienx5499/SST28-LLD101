package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ExternalRequest;
import java.util.*;

public class LoadBalancingStrategy implements ElevatorSelectionStrategy {

  @Override
  public Elevator selectElevator(ExternalRequest request, List<Elevator> availableElevators) {
    if (availableElevators.isEmpty()) {
      return null;
    }

    Elevator leastLoaded = null;
    double minLoad = Double.MAX_VALUE;

    for (Elevator elevator : availableElevators) {
      if (!elevator.isAvailable()) continue;

      if (elevator.getCurrentLoad() < minLoad) {
        minLoad = elevator.getCurrentLoad();
        leastLoaded = elevator;
      }
    }

    return leastLoaded;
  }
}
