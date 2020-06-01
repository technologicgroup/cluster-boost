package com.technologicgroup.boost.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.lang.IgniteRunnable;

@Slf4j
@RequiredArgsConstructor
class RunnableBeanProvider<T extends Runnable> implements IgniteRunnable {
    private final Class<T> beanClass;

    private T getBean() {
        return ContextHolder.getContext().getBean(beanClass);
    }

    @Override
    public void run() {
        try {
            getBean().run();
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            throw th;
        }
    }
}
