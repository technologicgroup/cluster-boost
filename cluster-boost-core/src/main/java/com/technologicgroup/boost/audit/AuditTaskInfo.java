package com.technologicgroup.boost.audit;

import lombok.Value;

import java.sql.Timestamp;

@Value
public class AuditTaskInfo {
  Timestamp start;
  Timestamp end;
  String message;
  String detailedMessage;
  int resultCode;
  Class<?> beanClass;
}
