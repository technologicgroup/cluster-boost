package com.technologicgroup.boost.audit;

import com.technologicgroup.boost.audit.providers.BeanProviderAuditFactory;
import com.technologicgroup.boost.audit.service.ServiceConfig;
import com.technologicgroup.boost.chain.ChainAuditConfig;
import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Package configuration
 */
@Configuration
@ComponentScan(basePackageClasses = { ServiceConfig.class, ChainAuditConfig.class })
public class ClusterAuditConfig {

  @Bean
  @Primary
  public BeanProviderFactory beanProviderFactory() {
      return new BeanProviderAuditFactory();
  }

  @Bean
  public AuditService auditService(AuditItemAccessor itemAccessor) {
    return new AuditService(itemAccessor);
  }
}
