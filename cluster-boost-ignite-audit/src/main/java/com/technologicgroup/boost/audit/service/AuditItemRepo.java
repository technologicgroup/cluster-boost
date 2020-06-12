package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemRepository;
import com.technologicgroup.boost.common.CommonRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for audit items
 */
@Repository
class AuditItemRepo extends CommonRepository<String, AuditItem> implements AuditItemRepository {

}
