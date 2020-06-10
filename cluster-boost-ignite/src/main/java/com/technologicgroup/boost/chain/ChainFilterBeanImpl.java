package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Performs filtering during execution of the chain steps
 * All collected results from previous steps need to be filtered
 * to pass proper results for a proper node
 * @param <A> is an execution argument class
 */
@Service
@RequiredArgsConstructor
class ChainFilterBeanImpl<A> implements ChainFilterBean<A> {

  private final Cluster cluster;

  /**
   * Searches for a result related to current node and pass it as a result of the filtering
   * @param arg is a task argument
   * @return the result that related to the current local node
   */
  @Override
  public A run(Collection<ChainResult<A>> arg) {
    return arg.stream()
        .filter(a -> a.getNodeId().equals(cluster.getLocalNode()))
        .map(ChainResult::getResult)
        .findFirst()
        .orElse(null);
  }
}
