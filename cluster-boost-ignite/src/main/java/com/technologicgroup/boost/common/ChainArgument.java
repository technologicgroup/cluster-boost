package com.technologicgroup.boost.common;

import lombok.Value;

import java.util.List;

@Value
class ChainArgument<A> {
  A arg;
  List<ChainStep<?, ?>> items;
}
