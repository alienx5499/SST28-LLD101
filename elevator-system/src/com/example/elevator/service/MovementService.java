package com.example.elevator.service;

import com.example.elevator.domain.*;
import com.example.elevator.domain.strategy.MovementStrategy;
import com.example.elevator.domain.strategy.ScanStrategy;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MovementService {
  private final ElevatorService elevatorService;
  private final RequestService requestService;
  private final BuildingService buildingService;
  private final DispatcherService dispatcherService;
  private MovementStrategy movementStrategy;
  private ScheduledExecutorService scheduler;
  private volatile boolean systemRunning = false;

  public MovementService(
      ElevatorService elevatorService,
      RequestService requestService,
      BuildingService buildingService,
      DispatcherService dispatcherService) {
    this.elevatorService = elevatorService;
    this.requestService = requestService;
    this.buildingService = buildingService;
    this.dispatcherService = dispatcherService;
    this.movementStrategy = new ScanStrategy();
  }

  public void startElevatorSystem(String buildingId) {
    systemRunning = true;
    buildingService.setBuildingSystemState(buildingId, SystemState.RUNNING);

    scheduler = Executors.newScheduledThreadPool(2);

    scheduler.scheduleAtFixedRate(
        () -> {
          if (systemRunning) {

            dispatcherService.processPendingRequests(buildingId);
          }
        },
        0,
        1,
        TimeUnit.SECONDS);

    scheduler.scheduleAtFixedRate(
        () -> {
          if (systemRunning) {
            processAllElevatorMovements(buildingId);
          }
        },
        0,
        2,
        TimeUnit.SECONDS);

    System.out.println("Elevator system started for building: " + buildingId);
  }

  public void stopElevatorSystem(String buildingId) {
    buildingService.setBuildingSystemState(buildingId, SystemState.STOPPING);
    System.out.println("Stopping elevator system gracefully...");

    while (hasPendingRequests(buildingId)) {
      processAllElevatorMovements(buildingId);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }

    systemRunning = false;
    if (scheduler != null) {
      scheduler.shutdown();
    }
    buildingService.setBuildingSystemState(buildingId, SystemState.STOPPED);
    System.out.println("Elevator system stopped gracefully");
  }

  public void triggerEmergency(String buildingId, EmergencyType type) {
    systemRunning = false;

    SystemState emergencyState =
        type == EmergencyType.POWER_OUTAGE ? SystemState.POWER_OUTAGE : SystemState.EMERGENCY_STOP;
    buildingService.setBuildingSystemState(buildingId, emergencyState);

    if (scheduler != null) {
      scheduler.shutdownNow();
    }


    for (Elevator elevator : elevatorService.getAllElevators(buildingId)) {
      elevator.setActive(false);
      elevator.setDirection(Direction.IDLE);
      elevator.setState(ElevatorState.STOPPED);
      elevator.setStateHandler(new com.example.elevator.domain.state.StoppedState());
      elevatorService.saveElevator(elevator);


      elevator.openDoors();
      elevatorService.saveElevator(elevator);
    }

    System.out.println("Emergency triggered for building " + buildingId + " (" + type + ")");
  }

  public void processAllElevatorMovements(String buildingId) {
    List<Elevator> elevators = elevatorService.getAllElevators(buildingId);

    for (Elevator elevator : elevators) {
      if (elevator.isActive()) {
        processElevatorMovement(elevator.getId(), elevator);
      }
    }
  }

  public void processElevatorMovement(String elevatorId, Elevator elevator) {
    List<InternalRequest> pendingRequests =
        requestService.getPendingRequestsForElevator(elevatorId);

    if (pendingRequests.isEmpty()) {

      if (elevator.getStateHandler()
          instanceof com.example.elevator.domain.state.PreMaintenanceState) {

        com.example.elevator.domain.state.PreMaintenanceState preMaintenanceState =
            (com.example.elevator.domain.state.PreMaintenanceState) elevator.getStateHandler();
        preMaintenanceState.checkForMaintenanceTransition(elevator);
        elevatorService.updateElevatorState(elevatorId, elevator.getState());
        return;
      }

      if (elevator.getDirection() != Direction.IDLE) {
        elevator.setDirection(Direction.IDLE);
        elevator.setState(ElevatorState.STOPPED);
        elevatorService.updateElevatorState(elevatorId, ElevatorState.STOPPED);
      }
      return;
    }

    List<Integer> path = movementStrategy.calculatePath(elevator, pendingRequests);

    if (path.isEmpty()) {
      return;
    }

    if (elevator.isFull()) {
      System.out.print("\u0007");
      System.out.println(
          "Weight limit exceeded for elevator " + elevatorId + ". Keeping doors open.");
      elevator.setState(ElevatorState.STOPPED);
      elevator.setStateHandler(new com.example.elevator.domain.state.StoppedState());
      elevatorService.saveElevator(elevator);
      elevator.openDoors();
      elevatorService.saveElevator(elevator);
      return;
    }

    int currentFloor = elevator.getCurrentFloor();
    int nextFloor = path.get(0);
    if (nextFloor == currentFloor && path.size() > 1) {
      nextFloor = path.get(1);
    }

    if (nextFloor == currentFloor) {
      return;
    }

    Direction newDirection = nextFloor > currentFloor ? Direction.UP : Direction.DOWN;
    elevator.setDirection(newDirection);
    elevator.setState(ElevatorState.MOVING);
    elevator.setStateHandler(new com.example.elevator.domain.state.MovingState());
    elevatorService.saveElevator(elevator);

    elevatorService.updateElevatorFloor(elevatorId, nextFloor);
    elevator.setCurrentFloor(nextFloor);

    for (InternalRequest request : pendingRequests) {
      if (request.getDestinationFloor() == nextFloor) {
        requestService.completeInternalRequest(request.getId());
        elevatorService.adjustElevatorLoad(elevatorId, -request.getPassengerWeightKg());
      }
    }

    elevator.setState(ElevatorState.STOPPED);
    elevator.setStateHandler(new com.example.elevator.domain.state.StoppedState());
    elevatorService.saveElevator(elevator);

    elevator.openDoors();
    elevatorService.saveElevator(elevator);
    elevator.closeDoors();
    elevator.setState(ElevatorState.STOPPED);
    elevator.setStateHandler(new com.example.elevator.domain.state.StoppedState());
    elevatorService.saveElevator(elevator);
  }

  private boolean hasPendingRequests(String buildingId) {
    List<Elevator> elevators = elevatorService.getAllElevators(buildingId);

    for (Elevator elevator : elevators) {
      List<InternalRequest> pendingRequests =
          requestService.getPendingRequestsForElevator(elevator.getId());
      if (!pendingRequests.isEmpty()) {
        return true;
      }
    }

    return dispatcherService.getQueueSize() > 0;
  }

  public void setMovementStrategy(MovementStrategy strategy) {
    this.movementStrategy = strategy;
  }
}
