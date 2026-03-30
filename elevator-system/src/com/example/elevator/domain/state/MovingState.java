package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;

public class MovingState implements ElevatorStateHandler {

  @Override
  public void openDoors(Elevator elevator) {

    System.out.println("Cannot open doors while elevator " + elevator.getId() + " is moving");
  }

  @Override
  public void closeDoors(Elevator elevator) {

    System.out.println("Elevator " + elevator.getId() + " doors already closed while moving");
  }

  @Override
  public void enterMaintenance(Elevator elevator) {

    elevator.setStateHandler(new PreMaintenanceState());
    System.out.println(
        "Elevator "
            + elevator.getId()
            + " entering pre-maintenance mode - will complete current movement");
  }

  @Override
  public void exitMaintenance(Elevator elevator) {

    System.out.println("Elevator " + elevator.getId() + " is not in maintenance mode");
  }

  @Override
  public boolean canAcceptExternalRequests(Elevator elevator) {
    return elevator.isActive() && !elevator.isFull();
  }

  @Override
  public boolean canAcceptInternalRequests(Elevator elevator) {
    return elevator.isActive();
  }

  @Override
  public String getStateName() {
    return "MOVING";
  }
}
