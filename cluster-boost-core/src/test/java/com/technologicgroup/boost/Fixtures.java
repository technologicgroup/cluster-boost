package com.technologicgroup.boost;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditTaskInfo;

import java.sql.Timestamp;
import java.util.UUID;

public class Fixtures {
  public static AuditItem auditItem(String trackingId, Timestamp start) {
    AuditTaskInfo taskInfo = new AuditTaskInfo(start, new Timestamp(start.getTime() + 100L), null, null, Object.class);
    return new AuditItem(UUID.randomUUID().toString(), trackingId, taskInfo, UUID.randomUUID().toString());
  }
}
