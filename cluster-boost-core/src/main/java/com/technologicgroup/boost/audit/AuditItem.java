package com.technologicgroup.boost.audit;

import lombok.Value;

import java.io.Serializable;

/**
 * Represents an audit item related to tracking identifier
 */
@Value
public class AuditItem implements Serializable {
  String id;
  String trackingId;
  AuditTaskInfo taskInfo;
  String nodeId;
}
