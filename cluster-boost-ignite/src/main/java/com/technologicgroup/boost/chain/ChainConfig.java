package com.technologicgroup.boost.chain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Package configuration
 */
@Configuration
@ComponentScan(basePackageClasses = { ChainBeanImpl.class, ChainFilterBeanImpl.class })
public class ChainConfig {

}
