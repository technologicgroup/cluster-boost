package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditNodeItem;
import com.technologicgroup.boost.audit.AuditNodeItemService;
import com.technologicgroup.boost.common.CommonDataService;
import org.springframework.stereotype.Service;

@Service
class AuditNodeItemServiceImpl extends CommonDataService<String, AuditNodeItem> implements AuditNodeItemService {
  public AuditNodeItemServiceImpl(AuditNodeItemRepo repository) {
    super(repository);
  }
}
