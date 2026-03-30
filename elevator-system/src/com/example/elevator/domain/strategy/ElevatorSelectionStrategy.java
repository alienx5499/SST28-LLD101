package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ExternalRequest;
import java.util.*;

public interface ElevatorSelectionStrategy {
  Elevator selectElevator(ExternalRequest request, List<Elevator> availableElevators);
}
