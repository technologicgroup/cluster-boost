package com.technologicgroup.boost.common;

import org.springframework.context.ApplicationEvent;

/**
 * Cluster ready event fires locally on every node on cluster when cluster is ready for tasks
 */
public class ClusterReadyEvent extends ApplicationEvent {

    public ClusterReadyEvent(Object source) {
        super(source);
    }
}
