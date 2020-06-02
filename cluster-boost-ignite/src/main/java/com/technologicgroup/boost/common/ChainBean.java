package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
class ChainBean<A, R> implements ClusterTask<ChainArgument<A>, R> {

  @Override
  public R run(@NotNull ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      ClusterTask clusterTask = ContextHolder.getContext().getBean(chainItem.getBean());

      if (clusterTask == null) {
        throw new RuntimeException("Cannot find bean class: " + chainItem.getBean());
      }
      result = clusterTask.run(result);
    }
    return (R)result;
  }

}
