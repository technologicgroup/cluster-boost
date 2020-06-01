package com.technologicgroup.boost.common;

import org.springframework.context.ApplicationEvent;

public class ClusterReadyEvent extends ApplicationEvent {

    public ClusterReadyEvent(Object source) {
        super(source);
    }
}
