package com.technologicgroup.boost.audit;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class AuditData {
  String trackingId;
  Timestamp createdAt;
  String beanClass;
  String startNode;
}
