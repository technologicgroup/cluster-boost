package com.technologicgroup.boost.chain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { ChainBeanImpl.class })
public class ChainConfig {

}
