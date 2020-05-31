package com.technologicgroup.boost.common;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class IgniteConfig {

    @Value("${cluster.hosts}")
    private String[] hosts;

    @Bean
    public Ignite ignite(IgniteConfiguration igniteConfiguration) {

        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList(hosts));

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setIpFinder(ipFinder);

        if (igniteConfiguration == null) {
            igniteConfiguration = new IgniteConfiguration();
        }

        igniteConfiguration.setDiscoverySpi(discoverySpi);
        return Ignition.start(igniteConfiguration);
    }
}
