package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditData;
import com.technologicgroup.boost.audit.AuditDataAccessor;
import com.technologicgroup.boost.common.CommonDataAccessor;
import org.springframework.stereotype.Service;

@Service
class AuditDataAccessorImpl extends CommonDataAccessor<String, AuditData> implements AuditDataAccessor {
  public AuditDataAccessorImpl(AuditDataRepo repository) {
    super(repository);
  }
}
