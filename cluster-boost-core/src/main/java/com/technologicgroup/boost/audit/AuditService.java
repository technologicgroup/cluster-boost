package com.technologicgroup.boost.audit;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class AuditService {
  private final AuditDataAccessor dataAccessor;
  private final AuditNodeItemAccessor itemAccessor;

  public AuditData getData(String trackingId) {
    return dataAccessor.networkGet(trackingId);
  }

  public Map<String, AuditNodeItem> getItems(String trackingId) {
    return itemAccessor.find(i -> i.getTrackingId().equals(trackingId));
  }

}
