package com.example.elevator.controller;

import com.example.elevator.domain.CallPriority;
import com.example.elevator.domain.Direction;
import com.example.elevator.domain.ExternalRequest;
import com.example.elevator.service.BuildingService;
import com.example.elevator.service.DispatcherService;
import com.example.elevator.service.RequestService;

public class FloorPanelController {
  private final RequestService requestService;
  private final BuildingService buildingService;
  private final DispatcherService dispatcherService;

  public FloorPanelController(ElevatorController elevatorController) {
    this.requestService = elevatorController.getRequestService();
    this.buildingService = elevatorController.getBuildingService();
    this.dispatcherService = elevatorController.getDispatcherService();
  }

  public void pressUpButton(int floorNumber, String buildingId) {
    if (!buildingService.isValidFloor(buildingId, floorNumber)) {
      throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
    }

    if (!buildingService.isSystemRunning(buildingId)) {
      System.out.println("Elevator system is not running. Request rejected.");
      return;
    }

    ExternalRequest request =
        requestService.createExternalRequest(floorNumber, Direction.UP, buildingId);
    dispatcherService.queueExternalRequest(request);

    System.out.println("UP button pressed on floor " + floorNumber);
  }

  public void pressUpButton(int floorNumber, String buildingId, CallPriority priority) {
    if (!buildingService.isValidFloor(buildingId, floorNumber)) {
      throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
    }

    if (!buildingService.isSystemRunning(buildingId)) {
      System.out.println("Elevator system is not running. Request rejected.");
      return;
    }

    ExternalRequest request =
        requestService.createExternalRequest(floorNumber, Direction.UP, buildingId, priority);
    dispatcherService.queueExternalRequest(request);

    System.out.println("UP button pressed on floor " + floorNumber + " (" + priority + ")");
  }

  public void pressDownButton(int floorNumber, String buildingId) {
    if (!buildingService.isValidFloor(buildingId, floorNumber)) {
      throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
    }

    if (!buildingService.isSystemRunning(buildingId)) {
      System.out.println("Elevator system is not running. Request rejected.");
      return;
    }

    ExternalRequest request =
        requestService.createExternalRequest(floorNumber, Direction.DOWN, buildingId);
    dispatcherService.queueExternalRequest(request);

    System.out.println("DOWN button pressed on floor " + floorNumber);
  }

  public void pressDownButton(int floorNumber, String buildingId, CallPriority priority) {
    if (!buildingService.isValidFloor(buildingId, floorNumber)) {
      throw new IllegalArgumentException("Invalid floor number: " + floorNumber);
    }

    if (!buildingService.isSystemRunning(buildingId)) {
      System.out.println("Elevator system is not running. Request rejected.");
      return;
    }

    ExternalRequest request =
        requestService.createExternalRequest(floorNumber, Direction.DOWN, buildingId, priority);
    dispatcherService.queueExternalRequest(request);

    System.out.println("DOWN button pressed on floor " + floorNumber + " (" + priority + ")");
  }
}
