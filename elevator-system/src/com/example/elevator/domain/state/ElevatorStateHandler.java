package com.example.elevator.domain.state;

import com.example.elevator.domain.Elevator;

public interface ElevatorStateHandler {

  void openDoors(Elevator elevator);

  void closeDoors(Elevator elevator);

  void enterMaintenance(Elevator elevator);

  void exitMaintenance(Elevator elevator);

  boolean canAcceptExternalRequests(Elevator elevator);

  boolean canAcceptInternalRequests(Elevator elevator);

  String getStateName();
}
