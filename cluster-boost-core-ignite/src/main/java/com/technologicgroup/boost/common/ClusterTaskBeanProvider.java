package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@AllArgsConstructor
class ClusterTaskBeanProvider<A, R, T extends ClusterTask<A, R>> implements IgniteCallable<R> {
    private final Class<T> beanClass;
    private final A arg;

    private T getBean() {
        return Activator.getContext().getBean(beanClass);
    }

    @Override
    public R call() {
        try {
            return getBean().run(arg);
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            throw th;
        }
    }
}
