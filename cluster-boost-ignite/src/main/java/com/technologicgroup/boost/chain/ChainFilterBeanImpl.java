package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
class ChainFilterBeanImpl<A> implements ChainFilterBean<A> {

  private final Cluster cluster;

  @Override
  public A run(Collection<ChainResult<A>> arg) {
    return arg.stream()
        .filter(a -> a.getNodeId().equals(cluster.getLocalNode()))
        .map(ChainResult::getResult)
        .findFirst()
        .orElse(null);
  }
}
