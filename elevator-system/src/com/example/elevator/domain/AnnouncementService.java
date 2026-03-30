package com.example.elevator.domain;

import java.util.*;

public interface AnnouncementService {
  void announce(String messageKey, Locale locale);

  default void announce(String messageKey) {
    announce(messageKey, Locale.getDefault());
  }
}
