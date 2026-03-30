package com.example.elevator.adapter;

import com.example.elevator.domain.AnnouncementService;
import java.util.*;

public class ConsoleAnnouncementService implements AnnouncementService {
  @Override
  public void announce(String messageKey, Locale locale) {
    System.out.println("[" + locale + "] " + messageKey);
  }
}
