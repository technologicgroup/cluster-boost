package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.audit.Trackable;
import lombok.Value;

import java.util.List;

/**
 * The transport object that contains a first chain argument and a list of chain steps
 * @param <A> is an argument class
 */
@Value
class ChainArgument<A> implements Trackable {
  A arg;
  List<ChainStep<?, ?>> items;
  String trackingId;
}
