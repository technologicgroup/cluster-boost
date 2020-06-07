package com.technologicgroup.boost.audit.service;

import com.technologicgroup.boost.audit.AuditData;
import com.technologicgroup.boost.audit.AuditDataRepository;
import com.technologicgroup.boost.common.CommonRepository;
import org.springframework.stereotype.Repository;

@Repository
class AuditDataRepo extends CommonRepository<String, AuditData> implements AuditDataRepository {

}
