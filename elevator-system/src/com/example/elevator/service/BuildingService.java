package com.example.elevator.service;

import com.example.elevator.domain.Building;
import com.example.elevator.domain.SystemState;
import com.example.elevator.repository.BuildingRepository;
import com.example.elevator.repository.impl.BuildingRepositoryImpl;

public class BuildingService {
  private final BuildingRepository buildingRepository;

  public BuildingService() {
    this.buildingRepository = new BuildingRepositoryImpl();
  }

  public Building createBuilding(String name, int minFloor, int maxFloor, int totalElevators) {
    Building building = new Building(name, minFloor, maxFloor, totalElevators);
    return buildingRepository.save(building);
  }

  public boolean isValidFloor(String buildingId, int floor) {
    return buildingRepository
        .findById(buildingId)
        .map(building -> building.isValidFloor(floor))
        .orElse(false);
  }

  public void setBuildingSystemState(String buildingId, SystemState state) {
    buildingRepository
        .findById(buildingId)
        .ifPresent(
            building -> {
              building.setSystemState(state);
              buildingRepository.save(building);
            });
  }

  public boolean isSystemRunning(String buildingId) {
    return buildingRepository
        .findById(buildingId)
        .map(building -> building.getSystemState() == SystemState.RUNNING)
        .orElse(false);
  }

  public Building findById(String buildingId) {
    return buildingRepository.findById(buildingId).orElse(null);
  }

  public boolean buildingExists(String buildingId) {
    return buildingRepository.findById(buildingId).isPresent();
  }

  public void addElevatorsToBuilding(String buildingId, int count) {
    buildingRepository
        .findById(buildingId)
        .ifPresent(
            building -> {
              building.addElevators(count);
              buildingRepository.save(building);
            });
  }

  public void addFloors(String buildingId, int newMaxFloor) {
    buildingRepository
        .findById(buildingId)
        .ifPresent(
            building -> {
              building.addFloors(newMaxFloor);
              buildingRepository.save(building);
            });
  }

  public void disableFloor(String buildingId, int floor) {
    buildingRepository
        .findById(buildingId)
        .ifPresent(
            building -> {
              building.disableFloor(floor);
              buildingRepository.save(building);
            });
  }

  public void enableFloor(String buildingId, int floor) {
    buildingRepository
        .findById(buildingId)
        .ifPresent(
            building -> {
              building.enableFloor(floor);
              buildingRepository.save(building);
            });
  }
}
