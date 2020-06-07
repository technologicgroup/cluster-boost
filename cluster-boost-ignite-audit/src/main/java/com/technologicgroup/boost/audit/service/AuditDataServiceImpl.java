package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditData;
import com.technologicgroup.boost.audit.AuditDataService;
import com.technologicgroup.boost.common.CommonDataService;
import org.springframework.stereotype.Service;

@Service
class AuditDataServiceImpl extends CommonDataService<String, AuditData> implements AuditDataService {
  public AuditDataServiceImpl(AuditDataRepo repository) {
    super(repository);
  }
}
