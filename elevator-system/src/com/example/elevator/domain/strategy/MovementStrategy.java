package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.InternalRequest;
import java.util.*;

public interface MovementStrategy {
  List<Integer> calculatePath(Elevator elevator, List<InternalRequest> requests);
}
