package com.technologicgroup.boost.audit;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class AuditNodeItem {
  String id;
  String trackingId;
  Timestamp start;
  Timestamp end;
  String message;
  String detailedMessage;
  int resultCode;
  String beanClass;
  String nodeId;
}
