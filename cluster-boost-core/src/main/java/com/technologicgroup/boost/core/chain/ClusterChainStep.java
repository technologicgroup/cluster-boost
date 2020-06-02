package com.technologicgroup.boost.core.chain;

import com.technologicgroup.boost.core.ClusterTask;

import java.util.Collection;

public interface ClusterChainStep<R> {
  Collection<R> run();
  <Res, T extends ClusterTask<R, Res>> ClusterChainStep<Res> map(Class<T> bean);
}
