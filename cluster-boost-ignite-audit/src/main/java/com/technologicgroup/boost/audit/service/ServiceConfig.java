package com.technologicgroup.boost.audit.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    AuditDataRepo.class,
    AuditDataServiceImpl.class,
    AuditNodeItemRepo.class,
    AuditNodeItemServiceImpl.class
})
public class ServiceConfig {

}
