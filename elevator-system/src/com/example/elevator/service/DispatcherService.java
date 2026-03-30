package com.example.elevator.service;

import com.example.elevator.domain.CallPriority;
import com.example.elevator.domain.Elevator;
import com.example.elevator.domain.ExternalRequest;
import com.example.elevator.domain.InternalRequest;
import com.example.elevator.domain.strategy.ElevatorSelectionStrategy;
import com.example.elevator.domain.strategy.NearestElevatorStrategy;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class DispatcherService {
  private final RequestService requestService;
  private final ElevatorService elevatorService;
  private ElevatorSelectionStrategy selectionStrategy;
  private final PriorityBlockingQueue<ExternalRequest> requestQueue;

  public DispatcherService(RequestService requestService, ElevatorService elevatorService) {
    this.requestService = requestService;
    this.elevatorService = elevatorService;
    this.selectionStrategy = new NearestElevatorStrategy();
    this.requestQueue =
        new PriorityBlockingQueue<ExternalRequest>(
            11,
            new Comparator<ExternalRequest>() {
              @Override
              public int compare(ExternalRequest a, ExternalRequest b) {
                int prA = priorityRank(a.getPriority());
                int prB = priorityRank(b.getPriority());
                int prCmp = Integer.compare(prB, prA);
                if (prCmp != 0) return prCmp;
                return Long.compare(a.getTimestamp(), b.getTimestamp());
              }
            });
  }

  private static int priorityRank(CallPriority priority) {
    if (priority == CallPriority.HIGH) return 3;
    if (priority == CallPriority.MEDIUM) return 2;
    return 1;
  }

  public void queueExternalRequest(ExternalRequest request) {
    requestQueue.add(request);
    System.out.println(
        "Request queued: Floor " + request.getFloorNumber() + " " + request.getDirection());
  }

  public Elevator selectBestElevator(ExternalRequest request, List<Elevator> availableElevators) {
    List<Elevator> eligible = new ArrayList<>();
    for (Elevator elevator : availableElevators) {
      if (!elevator.isExpress()) {
        eligible.add(elevator);
        continue;
      }
      if (elevator.getExpressStops().contains(request.getFloorNumber())) {
        eligible.add(elevator);
      }
    }
    return selectionStrategy.selectElevator(request, eligible);
  }

  public void assignRequestToElevator(ExternalRequest request, Elevator elevator) {
    elevatorService.adjustElevatorLoad(
        elevator.getId(), InternalRequest.DEFAULT_PASSENGER_WEIGHT_KG);
    requestService.assignRequestToElevator(request.getId(), elevator.getId());
    System.out.println(
        "Request assigned: Floor " + request.getFloorNumber() + " -> Elevator " + elevator.getId());
  }

  public void processPendingRequests(String buildingId) {

    while (!requestQueue.isEmpty()) {
      try {
        ExternalRequest request = requestQueue.take();
        if (!request.getBuildingId().equals(buildingId)) {
          requestQueue.put(request);
          break;
        }

        List<Elevator> availableElevators = elevatorService.getAvailableElevators(buildingId);
        Elevator selectedElevator = selectBestElevator(request, availableElevators);

        if (selectedElevator != null) {
          assignRequestToElevator(request, selectedElevator);
        } else {

          requestQueue.put(request);
          break;
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  public void setElevatorSelectionStrategy(ElevatorSelectionStrategy strategy) {
    this.selectionStrategy = strategy;
  }

  public int getQueueSize() {
    return requestQueue.size();
  }

  public void processExternalRequest(ExternalRequest request, String buildingId) {
    List<Elevator> availableElevators = elevatorService.getAvailableElevators(buildingId);
    Elevator selectedElevator = selectBestElevator(request, availableElevators);

    if (selectedElevator != null) {
      assignRequestToElevator(request, selectedElevator);
    } else {
      queueExternalRequest(request);
    }
  }
}
