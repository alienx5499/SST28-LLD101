package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ElevatorState;

public class PreMaintenanceState implements ElevatorStateHandler {

  public void checkForMaintenanceTransition(Elevator elevator) {

    System.out.println(
        "Pre-maintenance elevator "
            + elevator.getId()
            + " has no more pending requests - entering full maintenance mode");
    elevator.setState(ElevatorState.MAINTENANCE);
    elevator.setStateHandler(new MaintenanceState());
    elevator.setActive(false);
  }

  @Override
  public void openDoors(Elevator elevator) {
    elevator.setState(ElevatorState.DOORS_OPENING);

    System.out.println(
        "Pre-maintenance elevator " + elevator.getId() + " opening doors for passenger exit");
  }

  @Override
  public void closeDoors(Elevator elevator) {
    elevator.setState(ElevatorState.DOORS_CLOSING);
    System.out.println("Pre-maintenance elevator " + elevator.getId() + " closing doors");
  }

  @Override
  public void enterMaintenance(Elevator elevator) {

    elevator.setState(ElevatorState.MAINTENANCE);
    elevator.setStateHandler(new MaintenanceState());
    elevator.setActive(false);
    System.out.println(
        "Elevator " + elevator.getId() + " transitioning from pre-maintenance to full maintenance");
  }

  @Override
  public void exitMaintenance(Elevator elevator) {

    elevator.setState(ElevatorState.STOPPED);
    elevator.setStateHandler(new StoppedState());
    elevator.setActive(true);
    System.out.println("Elevator " + elevator.getId() + " exiting pre-maintenance mode");
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
    return "PRE_MAINTENANCE";
  }
}
