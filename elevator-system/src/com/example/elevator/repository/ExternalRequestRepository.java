package com.example.elevator.repository;

import com.example.elevator.domain.ExternalRequest;
import com.example.elevator.domain.RequestStatus;
import java.util.*;

public interface ExternalRequestRepository {
  ExternalRequest save(ExternalRequest request);

  List<ExternalRequest> findPendingRequests(String buildingId);

  List<ExternalRequest> findQueuedRequests(String buildingId);

  void updateRequestStatus(String requestId, RequestStatus status);

  Optional<ExternalRequest> findById(String requestId);

  List<ExternalRequest> findAll();

  void deleteById(String requestId);
}
