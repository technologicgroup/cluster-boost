package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChainStep<A, R> {

  @Getter(value = AccessLevel.PACKAGE)
  private final Class<? extends ClusterTask<A, R>> bean;

  private final Chain chain;

  public Collection<R> run() {
    return chain.run();
  }

  public <Res> Res collect(Function<Collection<R>, Res> function) {
    return function.apply(chain.run());
  }

  public <Res, NR> ChainStep<Res, NR> reduce(Function<Collection<R>, Res> function) {
    Collection<R> results = chain.run();
    chain.arg = function.apply(results);
    return new ChainStep<>(null, chain);
  }

  public <Any, Res, T extends ClusterTask<Any, Res>> ChainStep<Any, Res> reduce(Class<T> bean) {
    chain.run();
    chain.arg = null;
    return new ChainStep<>(bean, chain);
  }

  public <Res, NR, T extends ClusterTask<Res, NR>> ChainStep<Res, NR> reduce(Function<Collection<R>, Res> function,
                                                                             Class<T> bean) {
    Collection<R> results = chain.run();
    chain.arg = function.apply(results);
    return new ChainStep<>(bean, chain);
  }

  public <Res, T extends ClusterTask<R, Res>> ChainStep<R, Res> map(Class<T> bean) {
    ChainStep<R, Res> step = new ChainStep<>(bean, chain);
    chain.steps.add(step);
    return step;
  }

}
