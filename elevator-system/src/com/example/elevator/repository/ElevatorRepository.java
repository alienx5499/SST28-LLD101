package com.example.elevator.repository;

import com.example.elevator.domain.Elevator;
import java.util.*;

public interface ElevatorRepository {
  Elevator save(Elevator elevator);

  Optional<Elevator> findById(String elevatorId);

  List<Elevator> findByBuilding(String buildingId);

  List<Elevator> findAvailableElevators(String buildingId);

  void deleteById(String elevatorId);
}
