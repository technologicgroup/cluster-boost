package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChainStep<A, R> {

  @Getter(value = AccessLevel.PACKAGE)
  private final Class<? extends ClusterTask<A, R>> bean;

  private final Chain chain;

  public Collection<R> run() {
    return chain.run();
  }

  public <Res, T extends ClusterTask<R, Res>> ChainStep<R, Res> map(Class<T> bean) {
    ChainStep<R, Res> step = new ChainStep<>(bean, chain);
    chain.steps.add(step);
    return step;
  }

}
