package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditNodeItem;
import com.technologicgroup.boost.audit.AuditNodeItemAccessor;
import com.technologicgroup.boost.common.CommonDataAccessor;
import org.springframework.stereotype.Service;

@Service
class AuditNodeItemAccessorImpl extends CommonDataAccessor<String, AuditNodeItem> implements AuditNodeItemAccessor {
  public AuditNodeItemAccessorImpl(AuditNodeItemRepo repository) {
    super(repository);
  }
}
