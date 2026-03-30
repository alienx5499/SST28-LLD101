package com.example.elevator.domain.strategy;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.InternalRequest;
import java.util.*;

public class FCFSStrategy implements MovementStrategy {

  @Override
  public List<Integer> calculatePath(Elevator elevator, List<InternalRequest> requests) {
    List<Integer> path = new ArrayList<>();

    requests.sort((r1, r2) -> Long.compare(r1.getTimestamp(), r2.getTimestamp()));

    for (InternalRequest request : requests) {
      if (!path.contains(request.getDestinationFloor())) {
        path.add(request.getDestinationFloor());
      }
    }

    return path;
  }
}
