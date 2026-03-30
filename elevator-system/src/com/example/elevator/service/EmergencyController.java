package com.example.elevator.service;

import com.example.elevator.domain.EmergencyType;

public class EmergencyController {
  private final MovementService movementService;

  public EmergencyController(MovementService movementService) {
    this.movementService = movementService;
  }

  public void triggerEmergency(String buildingId, EmergencyType type) {
    movementService.triggerEmergency(buildingId, type);
  }
}
