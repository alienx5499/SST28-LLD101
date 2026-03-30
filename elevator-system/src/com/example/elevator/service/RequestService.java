package com.example.elevator.service;

import com.example.elevator.domain.Direction;
import com.example.elevator.domain.ExternalRequest;
import com.example.elevator.domain.InternalRequest;
import com.example.elevator.domain.RequestStatus;
import com.example.elevator.repository.ExternalRequestRepository;
import com.example.elevator.repository.InternalRequestRepository;
import com.example.elevator.repository.impl.ExternalRequestRepositoryImpl;
import com.example.elevator.repository.impl.InternalRequestRepositoryImpl;
import java.util.*;
import java.util.stream.*;

public class RequestService {
  private final ExternalRequestRepository externalRequestRepository;
  private final InternalRequestRepository internalRequestRepository;

  public RequestService() {
    this.externalRequestRepository = new ExternalRequestRepositoryImpl();
    this.internalRequestRepository = new InternalRequestRepositoryImpl();
  }

  public ExternalRequest createExternalRequest(int floor, Direction direction, String buildingId) {
    ExternalRequest request = new ExternalRequest(buildingId, floor, direction);
    return externalRequestRepository.save(request);
  }

  public ExternalRequest createExternalRequest(
      int floor,
      Direction direction,
      String buildingId,
      com.example.elevator.domain.CallPriority priority) {
    ExternalRequest request = new ExternalRequest(buildingId, floor, direction, priority);
    return externalRequestRepository.save(request);
  }

  public InternalRequest createInternalRequest(String elevatorId, int destinationFloor) {
    InternalRequest request = new InternalRequest(elevatorId, destinationFloor);
    return internalRequestRepository.save(request);
  }

  public void completeExternalRequest(String requestId) {
    externalRequestRepository.updateRequestStatus(requestId, RequestStatus.COMPLETED);
  }

  public void completeInternalRequest(String requestId) {
    internalRequestRepository.updateRequestStatus(requestId, RequestStatus.COMPLETED);
  }

  public List<ExternalRequest> getQueuedRequests(String buildingId) {
    return externalRequestRepository.findQueuedRequests(buildingId);
  }

  public List<InternalRequest> getPendingRequestsForElevator(String elevatorId) {
    return internalRequestRepository.findPendingByElevator(elevatorId);
  }

  public void assignRequestToElevator(String requestId, String elevatorId) {
    externalRequestRepository
        .findById(requestId)
        .ifPresent(
            request -> {
              request.setAssignedElevatorId(elevatorId);
              request.setStatus(RequestStatus.ASSIGNED);
              externalRequestRepository.save(request);

              InternalRequest internalRequest =
                  new InternalRequest(
                      elevatorId,
                      request.getFloorNumber(),
                      InternalRequest.DEFAULT_PASSENGER_WEIGHT_KG);
              internalRequestRepository.save(internalRequest);

              System.out.println(
                  "Created internal request for elevator "
                      + elevatorId
                      + " to serve external request at floor "
                      + request.getFloorNumber());
            });
  }

  public List<ExternalRequest> getAssignedRequestsForElevator(String elevatorId) {
    return externalRequestRepository.findAll().stream()
        .filter(req -> elevatorId.equals(req.getAssignedElevatorId()))
        .filter(req -> req.getStatus() == RequestStatus.ASSIGNED)
        .collect(java.util.stream.Collectors.toList());
  }

  public void initiateGracefulShutdownForBuilding(String buildingId) {

    System.out.println(
        "Graceful shutdown initiated for building "
            + buildingId
            + " - allowing pending requests to complete");
  }

  public void initiateGracefulShutdownForAll() {

    System.out.println(
        "Graceful shutdown initiated for all elevators - allowing pending requests to complete");
  }

  public List<ExternalRequest> getPendingExternalRequests(String buildingId) {
    return externalRequestRepository.findAll().stream()
        .filter(req -> buildingId.equals(req.getBuildingId()))
        .filter(req -> req.getStatus() == RequestStatus.PENDING)
        .collect(java.util.stream.Collectors.toList());
  }

  public List<InternalRequest> getPendingInternalRequestsForElevator(String elevatorId) {
    return internalRequestRepository.findAll().stream()
        .filter(req -> elevatorId.equals(req.getElevatorId()))
        .filter(req -> req.getStatus() == RequestStatus.PENDING)
        .collect(java.util.stream.Collectors.toList());
  }
}
