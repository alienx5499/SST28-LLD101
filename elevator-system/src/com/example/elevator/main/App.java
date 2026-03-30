package com.example.elevator.main;

import com.example.elevator.controller.ElevatorController;
import com.example.elevator.controller.ElevatorPanelController;
import com.example.elevator.controller.FloorPanelController;
import com.example.elevator.domain.Building;
import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.strategy.LoadBalancingStrategy;

public class App {

  public static void main(String[] args) {

    ElevatorController elevatorController = new ElevatorController();
    FloorPanelController floorPanelController = new FloorPanelController(elevatorController);
    ElevatorPanelController elevatorPanelController =
        new ElevatorPanelController(elevatorController);

    try {
      System.out.println("=== ELEVATOR SYSTEM SIMULATION ===");

      Building building =
          elevatorController.getBuildingService().createBuilding("Tech Tower", 1, 10, 3);
      String buildingId = building.getId();
      System.out.println(
          "Created building: "
              + building.getName()
              + " (Floors: "
              + building.getMinFloor()
              + "-"
              + building.getMaxFloor()
              + ")");

      Elevator elevator1 = elevatorController.createElevator(buildingId, 8);
      Elevator elevator2 = elevatorController.createElevator(buildingId, 8);
      Elevator elevator3 = elevatorController.createElevator(buildingId, 8);

      System.out.println("Created 3 elevators with capacity 8 each");
      System.out.println(
          "Elevator IDs: "
              + elevator1.getId()
              + ", "
              + elevator2.getId()
              + ", "
              + elevator3.getId());

      elevatorController
          .getDispatcherService()
          .setElevatorSelectionStrategy(new LoadBalancingStrategy());
      System.out.println("Set elevator selection strategy to Load Balancing");

      elevatorController.startElevatorSystem(buildingId);
      Thread.sleep(1000);

      System.out.println("\n=== SIMULATING EXTERNAL REQUESTS ===");
      floorPanelController.pressUpButton(3, buildingId);
      floorPanelController.pressUpButton(7, buildingId);
      floorPanelController.pressDownButton(9, buildingId);
      floorPanelController.pressUpButton(2, buildingId);

      Thread.sleep(3000);

      System.out.println("\n=== SIMULATING INTERNAL REQUESTS ===");
      elevatorPanelController.selectFloor(elevator1.getId(), 5);
      elevatorPanelController.selectFloor(elevator1.getId(), 8);
      elevatorPanelController.selectFloor(elevator2.getId(), 4);
      elevatorPanelController.selectFloor(elevator3.getId(), 6);

      Thread.sleep(5000);

      elevatorPanelController.selectFloor(elevator1.getId(), 9);
      floorPanelController.pressUpButton(4, buildingId);
      elevatorPanelController.selectFloor(elevator2.getId(), 10);

      Thread.sleep(3000);

      System.out.println("\n=== TESTING FULL MAINTENANCE MODE ===");
      elevatorController.setElevatorMaintenance(elevator2.getId(), true);

      elevatorPanelController.selectFloor(elevator2.getId(), 7);

      Thread.sleep(2000);

      floorPanelController.pressUpButton(6, buildingId);

      Thread.sleep(3000);

      System.out.println("\n=== EXITING MAINTENANCE MODE ===");
      elevatorController.setElevatorMaintenance(elevator2.getId(), false);

      Thread.sleep(1000);

      System.out.println("\n=== STOPPING SYSTEM GRACEFULLY ===");
      elevatorController.stopElevatorSystem(buildingId);

      floorPanelController.pressUpButton(8, buildingId);

      System.out.println("\n=== SIMULATION COMPLETED ===");

    } catch (Exception e) {
      System.err.println("Error during simulation: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
