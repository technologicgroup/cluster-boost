package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
class ChainBeanImpl<A, R> implements ChainBean<A, R> {

  private final ApplicationContext context;

  @Override
  public ChainResult<R> run(@NotNull ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      if (chainItem.getBean() != null) {
        ClusterTask clusterTask = context.getBean(chainItem.getBean());

        if (clusterTask == null) {
          throw new RuntimeException("Cannot find bean class: " + chainItem.getBean());
        }

        result = clusterTask.run(result);

        // Interrupt chain if filter does not match
        if (clusterTask instanceof ChainFilterBean) {
          if (result == null) {
            break;
          }
        }

      }
    }
    return new ChainResult<>(context.getBean(Cluster.class).getLocalNode(), (R)result);
  }

}
