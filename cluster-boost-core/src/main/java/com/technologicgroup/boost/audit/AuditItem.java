package com.technologicgroup.boost.audit;

import lombok.Value;

@Value
public class AuditItem {
  String id;
  String trackingId;
  AuditTaskInfo taskInfo;
  String nodeId;
}
