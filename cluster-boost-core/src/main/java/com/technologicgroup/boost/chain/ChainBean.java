package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Abstract class represents the task bean that runs a chain of tasks on separate node
 * @param <A> is a class of the first chain step's argument
 * @param <R> is a class of the result of the chain tasks
 */
@RequiredArgsConstructor
public abstract class ChainBean<A, R> implements ClusterTask<ChainArgument<A>, ChainResult<R>> {

  protected final Cluster cluster;

  protected abstract <T> T getBean(Class<T> bean);

  @Override
  public ChainResult<R> run(ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();
    String trackingId = chainArgument.getTrackingId();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      if (chainItem.getBean() != null) {
        ClusterTask clusterTask = getBean(chainItem.getBean());

        String message = null;
        String detailedMessage = null;
        int resultCode = 0;
        Timestamp start = new Timestamp(System.currentTimeMillis());

        onStartBean(trackingId, start, chainItem.getBean(), cluster.getLocalNode());

        try {
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

        } catch (Exception e) {
          message = e.getMessage();
          detailedMessage = Optional.ofNullable(e.getCause()).map(Throwable::toString).orElse(null);
          resultCode = 100;
          throw e;
        } finally {
          Timestamp end = new Timestamp(System.currentTimeMillis());
          onFinishBean(trackingId, start, end, message, detailedMessage, resultCode, chainItem.getBean(), cluster.getLocalNode());
        }

      }
    }
    return new ChainResult<> (cluster.getLocalNode(), (R)result);
  }

  protected abstract void onFinishBean(String trackingId,
                                       Timestamp start,
                                       Timestamp end,
                                       String message,
                                       String detailedMessage,
                                       int resultCode,
                                       Class<? extends ClusterTask<?, ?>> bean,
                                       String localNode);

  protected abstract void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean, String localNode);


}
