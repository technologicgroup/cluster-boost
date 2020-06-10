package com.technologicgroup.boost.audit;

import lombok.Value;

/**
 * Represents an audit item related to tracking identifier
 */
@Value
public class AuditItem {
  String id;
  String trackingId;
  AuditTaskInfo taskInfo;
  String nodeId;
}
