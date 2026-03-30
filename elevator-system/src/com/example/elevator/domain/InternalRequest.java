package com.example.elevator.domain;

import java.util.*;

public class InternalRequest {
  private String id;
  private String elevatorId;
  private int destinationFloor;
  private long timestamp;
  private RequestStatus status;
  private double passengerWeightKg;

  public static final double DEFAULT_PASSENGER_WEIGHT_KG = 100.0;

  public InternalRequest(String elevatorId, int destinationFloor) {
    this(elevatorId, destinationFloor, DEFAULT_PASSENGER_WEIGHT_KG);
  }

  public InternalRequest(String elevatorId, int destinationFloor, double passengerWeightKg) {
    this.id = UUID.randomUUID().toString();
    this.elevatorId = elevatorId;
    this.destinationFloor = destinationFloor;
    this.timestamp = System.currentTimeMillis();
    this.status = RequestStatus.PENDING;
    this.passengerWeightKg = passengerWeightKg;
  }

  public String getId() {
    return id;
  }

  public String getElevatorId() {
    return elevatorId;
  }

  public int getDestinationFloor() {
    return destinationFloor;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public double getPassengerWeightKg() {
    return passengerWeightKg;
  }

  public void setStatus(RequestStatus status) {
    this.status = status;
  }
}
