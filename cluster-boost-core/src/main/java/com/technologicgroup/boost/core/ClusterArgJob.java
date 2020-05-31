package com.technologicgroup.boost.core;

public interface ClusterArgJob<A, R> {
    R run(A arg);
}
