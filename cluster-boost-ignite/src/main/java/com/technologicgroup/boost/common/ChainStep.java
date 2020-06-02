package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@Value
public class ChainStep<A, R> {
  Class<? extends ClusterTask<A, R>> bean;
  A arg;
  Chain chain;

  public Collection<R> run() {
    return chain.run();
  }

  public <Res, T extends ClusterTask<R, Res>> @NotNull ChainStep<R, Res> map(Class<T> bean) {
    ChainStep<R, Res> step = new ChainStep<R, Res>(bean, null, chain);
    chain.steps.add(step);
    return step;
  }

}
