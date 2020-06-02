package com.technologicgroup.boost.core.chain;

import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterTask;

public interface ClusterChain<A> {
  ClusterChain on(ClusterGroup clusterGroup);
  <R, T extends ClusterTask<A, R>> ClusterChainStep<R> start(Class<T> bean, A arg);
}
