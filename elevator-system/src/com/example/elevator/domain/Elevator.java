package com.example.elevator.domain;

import com.example.elevator.domain.state.ElevatorStateHandler;
import com.example.elevator.domain.state.StoppedState;
import java.util.*;

public class Elevator {
  private String id;
  private String buildingId;
  private int currentFloor;
  private Direction direction;
  private ElevatorState state;
  private double capacity;
  private double currentLoad;
  private boolean isActive;
  private ElevatorStateHandler stateHandler;
  private boolean express;
  private Set<Integer> expressStops;

  public Elevator(String buildingId, double capacity) {
    this.id = UUID.randomUUID().toString();
    this.buildingId = buildingId;
    this.currentFloor = 1;
    this.direction = Direction.IDLE;
    this.state = ElevatorState.STOPPED;
    this.capacity = capacity;
    this.currentLoad = 0;
    this.isActive = true;
    this.stateHandler = new StoppedState();
    this.express = false;
    this.expressStops = new HashSet<>();
  }

  public String getId() {
    return id;
  }

  public String getBuildingId() {
    return buildingId;
  }

  public int getCurrentFloor() {
    return currentFloor;
  }

  public Direction getDirection() {
    return direction;
  }

  public ElevatorState getState() {
    return state;
  }

  public double getCapacity() {
    return capacity;
  }

  public double getCurrentLoad() {
    return currentLoad;
  }

  public boolean isActive() {
    return isActive;
  }

  public ElevatorStateHandler getStateHandler() {
    return stateHandler;
  }

  public boolean isExpress() {
    return express;
  }

  public Set<Integer> getExpressStops() {
    return expressStops;
  }

  public void setExpressStops(Set<Integer> expressStops) {
    if (expressStops == null || expressStops.isEmpty()) {
      this.express = false;
      this.expressStops = new HashSet<>();
      return;
    }
    this.express = true;
    this.expressStops = new HashSet<>(expressStops);
  }

  public void setCurrentFloor(int currentFloor) {
    this.currentFloor = currentFloor;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public void setState(ElevatorState state) {
    this.state = state;
  }

  public void setCurrentLoad(double currentLoad) {
    this.currentLoad = currentLoad;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  public void setStateHandler(ElevatorStateHandler stateHandler) {
    this.stateHandler = stateHandler;
  }

  public boolean isAvailable() {
    return stateHandler.canAcceptExternalRequests(this);
  }

  public boolean canAcceptInternalRequests() {
    return stateHandler.canAcceptInternalRequests(this);
  }

  public boolean isFull() {
    return currentLoad > capacity;
  }

  public void openDoors() {
    stateHandler.openDoors(this);
  }

  public void closeDoors() {
    stateHandler.closeDoors(this);
  }

  public void enterMaintenance() {
    stateHandler.enterMaintenance(this);
  }

  public void exitMaintenance() {
    stateHandler.exitMaintenance(this);
  }

  public boolean isPreparingForMaintenance() {
    return stateHandler instanceof com.example.elevator.domain.state.PreMaintenanceState;
  }
}
