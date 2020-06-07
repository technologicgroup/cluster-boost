package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditNodeItem;
import com.technologicgroup.boost.audit.AuditNodeItemRepository;
import com.technologicgroup.boost.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
class AuditNodeItemRepo extends CommonRepository<String, AuditNodeItem> implements AuditNodeItemRepository {

}
