package com.technologicgroup.boost.core;

public interface ClusterTask<A, R> {
    R run(A arg);
}
