package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ElevatorState;

public class DoorsOpeningState implements ElevatorStateHandler {

  @Override
  public void openDoors(Elevator elevator) {

    System.out.println("Elevator " + elevator.getId() + " doors already opening");
  }

  @Override
  public void closeDoors(Elevator elevator) {
    elevator.setState(ElevatorState.DOORS_CLOSING);
    elevator.setStateHandler(new DoorsClosingState());
    System.out.println("Elevator " + elevator.getId() + " closing doors");
  }

  @Override
  public void enterMaintenance(Elevator elevator) {

    closeDoors(elevator);
    elevator.getStateHandler().enterMaintenance(elevator);
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
    return "DOORS_OPENING";
  }
}
