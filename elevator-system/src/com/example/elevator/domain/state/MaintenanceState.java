package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ElevatorState;

public class MaintenanceState implements ElevatorStateHandler {

  @Override
  public void openDoors(Elevator elevator) {

    elevator.setState(ElevatorState.DOORS_OPENING);
    System.out.println(
        "Maintenance elevator " + elevator.getId() + " opening doors for maintenance access");
  }

  @Override
  public void closeDoors(Elevator elevator) {

    elevator.setState(ElevatorState.DOORS_CLOSING);
    System.out.println(
        "Maintenance elevator " + elevator.getId() + " closing doors after maintenance access");
  }

  @Override
  public void enterMaintenance(Elevator elevator) {

    System.out.println("Elevator " + elevator.getId() + " already in maintenance mode");
  }

  @Override
  public void exitMaintenance(Elevator elevator) {

    elevator.setState(ElevatorState.STOPPED);
    elevator.setStateHandler(new StoppedState());
    elevator.setActive(true);
    System.out.println(
        "Elevator " + elevator.getId() + " exiting maintenance mode and returning to service");
  }

  @Override
  public boolean canAcceptExternalRequests(Elevator elevator) {

    return false;
  }

  @Override
  public boolean canAcceptInternalRequests(Elevator elevator) {

    return false;
  }

  @Override
  public String getStateName() {
    return "MAINTENANCE";
  }
}
