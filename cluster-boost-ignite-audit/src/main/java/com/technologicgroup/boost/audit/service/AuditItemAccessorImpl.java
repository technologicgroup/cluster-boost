package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
import com.technologicgroup.boost.common.CommonDataAccessor;
import org.springframework.stereotype.Service;

/**
 * Data accessor for audit items
 */
@Service
class AuditItemAccessorImpl extends CommonDataAccessor<String, AuditItem> implements AuditItemAccessor {
  public AuditItemAccessorImpl(AuditItemRepo repository) {
    super(repository);
  }
}
