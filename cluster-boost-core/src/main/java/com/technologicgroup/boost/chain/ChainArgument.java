package com.technologicgroup.boost.chain;

import lombok.Value;

import java.util.List;

@Value
public class ChainArgument<A> {
  A arg;
  List<ChainStep<?, ?>> items;
}
