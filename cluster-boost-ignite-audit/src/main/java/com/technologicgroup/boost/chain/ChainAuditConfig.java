package com.technologicgroup.boost.chain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Package configuration
 */
@Configuration
@ComponentScan(basePackageClasses = { ChainBeanAudit.class })
public class ChainAuditConfig {

}
