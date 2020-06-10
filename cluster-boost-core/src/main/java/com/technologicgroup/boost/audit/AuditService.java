package com.technologicgroup.boost.audit;

import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for audit items data manipulations
 */
@RequiredArgsConstructor
public class AuditService {
  private final AuditItemAccessor itemAccessor;

  /**
   * Finds all audit items by tracking id
   * @param trackingId is the tracking id that audit data can be linked to
   * @return a list of found audit items linked to trackingId
   */
  public List<AuditItem> getItems(String trackingId) {
    return itemAccessor.find(i -> i.getTrackingId().equals(trackingId))
        .values()
        .stream()
        .sorted(Comparator.comparing(c -> c.getTaskInfo().getStart()))
        .collect(Collectors.toList());
  }

}
