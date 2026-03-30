package com.example.elevator.repository;

import com.example.elevator.domain.Building;
import java.util.*;

public interface BuildingRepository {
  Building save(Building building);

  Optional<Building> findById(String buildingId);

  List<Building> findAll();

  void deleteById(String buildingId);
}
