package com.technologicgroup.boost.audit;

import lombok.Value;

import java.sql.Timestamp;

/**
 * Contains information about local node's task execution
 */
@Value
public class AuditTaskInfo {
  Timestamp start;
  Timestamp end;
  String message;
  String detailedMessage;
  Class<?> beanClass;
}
