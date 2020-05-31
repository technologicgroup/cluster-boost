package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterArgJob;
import com.technologicgroup.boost.core.ClusterJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteCallable;

@Slf4j
@AllArgsConstructor
class ClusterJobBeanArgProvider<A, R, T extends ClusterArgJob<A, R>> implements IgniteCallable<R> {
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
