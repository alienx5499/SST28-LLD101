package com.example.elevator.domain;

import java.util.*;

public class Building {
  private String id;
  private String name;
  private int minFloor;
  private int maxFloor;
  private int totalElevators;
  private SystemState systemState;
  private Set<Integer> disabledFloors;

  public Building(String name, int minFloor, int maxFloor, int totalElevators) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.minFloor = minFloor;
    this.maxFloor = maxFloor;
    this.totalElevators = totalElevators;
    this.systemState = SystemState.STOPPED;
    this.disabledFloors = new HashSet<>();
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getMinFloor() {
    return minFloor;
  }

  public int getMaxFloor() {
    return maxFloor;
  }

  public int getTotalElevators() {
    return totalElevators;
  }

  public void addElevators(int count) {
    this.totalElevators += count;
  }

  public SystemState getSystemState() {
    return systemState;
  }

  public void setSystemState(SystemState systemState) {
    this.systemState = systemState;
  }

  public void addFloors(int newMaxFloor) {
    if (newMaxFloor > this.maxFloor) {
      this.maxFloor = newMaxFloor;
    }
  }

  public void disableFloor(int floor) {
    disabledFloors.add(floor);
  }

  public void enableFloor(int floor) {
    disabledFloors.remove(floor);
  }

  public boolean isValidFloor(int floor) {
    return floor >= minFloor && floor <= maxFloor && !disabledFloors.contains(floor);
  }
}
