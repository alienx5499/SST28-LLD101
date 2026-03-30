package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ElevatorState;

public class StoppedState implements ElevatorStateHandler {

  @Override
  public void openDoors(Elevator elevator) {
    elevator.setState(ElevatorState.DOORS_OPENING);
    elevator.setStateHandler(new DoorsOpeningState());
    System.out.println(
        "Elevator " + elevator.getId() + " opening doors at floor " + elevator.getCurrentFloor());
  }

  @Override
  public void closeDoors(Elevator elevator) {
    System.out.println("Elevator " + elevator.getId() + " doors already closed");
  }

  @Override
  public void enterMaintenance(Elevator elevator) {
    elevator.setState(ElevatorState.MAINTENANCE);
    elevator.setStateHandler(new MaintenanceState());
    elevator.setActive(false);
    System.out.println("Elevator " + elevator.getId() + " entering maintenance mode");
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
    return "STOPPED";
  }
}
