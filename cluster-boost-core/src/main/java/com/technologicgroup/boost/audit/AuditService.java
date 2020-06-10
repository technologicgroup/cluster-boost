package com.technologicgroup.boost.audit;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AuditService {
  private final AuditItemAccessor itemAccessor;

  public List<AuditItem> getItems(String trackingId) {
    return itemAccessor.find(i -> i.getTrackingId().equals(trackingId))
        .values()
        .stream()
        .sorted(Comparator.comparing(c -> c.getTaskInfo().getStart()))
        .collect(Collectors.toList());
  }

}
