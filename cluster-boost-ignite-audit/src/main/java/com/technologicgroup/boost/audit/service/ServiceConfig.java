package com.technologicgroup.boost.audit.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Package configuration
 */
@Configuration
@ComponentScan(basePackageClasses = {
    AuditItemRepo.class,
    AuditItemAccessorImpl.class
})
public class ServiceConfig {

}
