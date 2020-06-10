package com.technologicgroup.boost.common.providers;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Package configuration
 */
@Configuration
@ComponentScan(basePackageClasses = { BeanProviderCommonFactory.class })
public class ProvidersConfig {

}
