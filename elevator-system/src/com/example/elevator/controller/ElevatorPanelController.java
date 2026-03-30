package com.example.elevator.controller;

import com.example.elevator.domain.Elevator;
import com.example.elevator.service.BuildingService;
import com.example.elevator.service.ElevatorService;
import com.example.elevator.service.RequestService;

public class ElevatorPanelController {
  private final RequestService requestService;
  private final ElevatorService elevatorService;
  private final BuildingService buildingService;

  public ElevatorPanelController(ElevatorController elevatorController) {
    this.requestService = elevatorController.getRequestService();
    this.elevatorService = elevatorController.getElevatorService();
    this.buildingService = elevatorController.getBuildingService();
  }

  public void selectFloor(String elevatorId, int destinationFloor) {
    Elevator elevator = elevatorService.findById(elevatorId);
    if (elevator == null) {
      throw new IllegalArgumentException("Elevator not found: " + elevatorId);
    }

    if (!buildingService.isValidFloor(elevator.getBuildingId(), destinationFloor)) {
      throw new IllegalArgumentException("Invalid floor number: " + destinationFloor);
    }

    if (elevator.getCurrentFloor() == destinationFloor) {
      System.out.println("Elevator is already on floor " + destinationFloor);
      return;
    }

    if (!elevator.canAcceptInternalRequests()) {
      System.out.println(
          "Elevator "
              + elevatorId
              + " cannot accept new requests in current state: "
              + elevator.getStateHandler().getStateName());
      return;
    }

    requestService.createInternalRequest(elevatorId, destinationFloor);
    System.out.println("Floor " + destinationFloor + " selected in elevator " + elevatorId);
  }

  public void openDoors(String elevatorId) {
    elevatorService.openDoors(elevatorId);
  }

  public void closeDoors(String elevatorId) {
    elevatorService.closeDoors(elevatorId);
  }
}
