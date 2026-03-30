package com.example.elevator.service;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ElevatorState;
import com.example.elevator.repository.ElevatorRepository;
import com.example.elevator.repository.impl.ElevatorRepositoryImpl;
import java.util.*;

public class ElevatorService {
  private final ElevatorRepository elevatorRepository;
  private RequestService requestService;

  public ElevatorService() {
    this.elevatorRepository = new ElevatorRepositoryImpl();
  }

  public void setRequestService(RequestService requestService) {
    this.requestService = requestService;
  }

  public Elevator createElevator(String buildingId, int capacity) {
    double weightLimitKg = capacity <= 0 ? 700.0 : capacity * 100.0;
    Elevator elevator = new Elevator(buildingId, weightLimitKg);
    return elevatorRepository.save(elevator);
  }

  public Elevator createExpressElevator(
      String buildingId, int capacity, Set<Integer> expressStops) {
    Elevator elevator = createElevator(buildingId, capacity);
    elevator.setExpressStops(expressStops);
    saveElevator(elevator);
    return elevator;
  }

  public void updateElevatorState(String elevatorId, ElevatorState state) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.setState(state);
              elevatorRepository.save(elevator);
            });
  }

  public void updateElevatorFloor(String elevatorId, int floor) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.setCurrentFloor(floor);
              elevatorRepository.save(elevator);
            });
  }

  public void adjustElevatorLoad(String elevatorId, double deltaKg) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.setCurrentLoad(elevator.getCurrentLoad() + deltaKg);
              elevatorRepository.save(elevator);
            });
  }

  public void saveElevator(Elevator elevator) {
    if (elevator == null) return;
    elevatorRepository.save(elevator);
  }

  public void openDoors(String elevatorId) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.openDoors();
              elevatorRepository.save(elevator);
            });
  }

  public void closeDoors(String elevatorId) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.closeDoors();
              elevatorRepository.save(elevator);
            });
  }

  public List<Elevator> getAvailableElevators(String buildingId) {
    return elevatorRepository.findAvailableElevators(buildingId);
  }

  public List<Elevator> getAllElevators(String buildingId) {
    return elevatorRepository.findByBuilding(buildingId);
  }

  public Elevator findById(String elevatorId) {
    return elevatorRepository.findById(elevatorId).orElse(null);
  }

  public void setMaintenanceMode(String elevatorId, boolean maintenance) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              if (maintenance) {

                if (hasPendingRequests(elevatorId) || hasAssignedRequests(elevatorId)) {
                  elevator.setStateHandler(
                      new com.example.elevator.domain.state.PreMaintenanceState());
                  System.out.println(
                      "Elevator "
                          + elevatorId
                          + " entering pre-maintenance mode to complete pending requests"
                          + " gracefully");
                } else {
                  elevator.enterMaintenance();
                  System.out.println(
                      "Elevator "
                          + elevatorId
                          + " entering maintenance mode directly (no pending requests)");
                }
              } else {
                elevator.exitMaintenance();
              }
              elevatorRepository.save(elevator);
            });
  }

  private boolean hasPendingRequests(String elevatorId) {
    if (requestService == null) {
      System.out.println("Warning: RequestService not injected, cannot check pending requests");
      return false;
    }
    List<com.example.elevator.domain.InternalRequest> pendingRequests =
        requestService.getPendingRequestsForElevator(elevatorId);
    return !pendingRequests.isEmpty();
  }

  private boolean hasAssignedRequests(String elevatorId) {
    if (requestService == null) {
      System.out.println("Warning: RequestService not injected, cannot check assigned requests");
      return false;
    }
    List<com.example.elevator.domain.ExternalRequest> assignedRequests =
        requestService.getAssignedRequestsForElevator(elevatorId);
    return !assignedRequests.isEmpty();
  }

  public List<Elevator> getElevatorsByBuilding(String buildingId) {
    return elevatorRepository.findByBuilding(buildingId);
  }

  public void setPreMaintenanceMode(String elevatorId) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              elevator.setStateHandler(new com.example.elevator.domain.state.PreMaintenanceState());
              elevatorRepository.save(elevator);
              System.out.println("Elevator " + elevatorId + " set to pre-maintenance mode");
            });
  }

  public void checkMaintenanceTransition(String elevatorId) {
    elevatorRepository
        .findById(elevatorId)
        .ifPresent(
            elevator -> {
              if (elevator.getStateHandler()
                  instanceof com.example.elevator.domain.state.PreMaintenanceState) {
                if (!hasPendingRequests(elevatorId)) {
                  elevator.enterMaintenance();
                  elevatorRepository.save(elevator);
                  System.out.println(
                      "Elevator " + elevatorId + " transitioned to maintenance mode");
                }
              }
            });
  }
}
