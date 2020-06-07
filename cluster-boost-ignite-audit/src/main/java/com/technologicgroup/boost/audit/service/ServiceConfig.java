package com.technologicgroup.boost.audit.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    AuditDataRepo.class,
    AuditDataAccessorImpl.class,
    AuditNodeItemRepo.class,
    AuditNodeItemAccessorImpl.class
})
public class ServiceConfig {

}
