package com.example.elevator.repository;

import com.example.elevator.domain.InternalRequest;
import com.example.elevator.domain.RequestStatus;
import java.util.*;

public interface InternalRequestRepository {
  InternalRequest save(InternalRequest request);

  List<InternalRequest> findByElevator(String elevatorId);

  List<InternalRequest> findPendingByElevator(String elevatorId);

  Optional<InternalRequest> findById(String requestId);

  List<InternalRequest> findAll();

  void updateRequestStatus(String requestId, RequestStatus status);
}
