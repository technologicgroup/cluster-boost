package com.technologicgroup.boost.chain;

import lombok.Value;

/**
 * Represents a result of chain execution for specified node
 * @param <R> is the result class
 */
@Value
public class ChainResult<R> {
  String nodeId;
  R result;
}
