package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Abstract class represents the task bean that runs a chain of steps on separate node
 * Every step represents a Spring bean that will be executed on a local node
 * @param <A> is a class of the first chain step's argument
 * @param <R> is a class of the result of the chain tasks
 */
@RequiredArgsConstructor
public abstract class ChainBean<A, R> implements ClusterTask<ChainArgument<A>, ChainResult<R>> {

  protected final Cluster cluster;

  /**
   * Returns a Spring bean for a bean class
   * @param bean is a class of a bean
   * @param <T> is a class of a bean
   * @return a Spring bean
   */
  protected abstract <T> T getBean(Class<T> bean);

  /**
   * Method will be executed on a local node to perform defined chain steps
   * @param chainArgument is an object that contains definition for chain steps
   * @return the chain result for the local node
   */
  @Override
  public ChainResult<R> run(ChainArgument<A> chainArgument) {
    Object result = chainArgument.getArg();
    String trackingId = chainArgument.getTrackingId();

    for (ChainStep<?, ?> chainItem : chainArgument.getItems()) {
      if (chainItem.getBean() != null) {
        ClusterTask clusterTask = getBean(chainItem.getBean());

        String message = null;
        String detailedMessage = null;
        Timestamp start = new Timestamp(System.currentTimeMillis());

        onStartBean(trackingId, start, chainItem.getBean());

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
          throw e;
        } finally {
          Timestamp end = new Timestamp(System.currentTimeMillis());
          onFinishBean(trackingId, start, end, message, detailedMessage, chainItem.getBean());
        }

      }
    }
    return new ChainResult<> (cluster.getLocalNode(), (R)result);
  }

  /**
   * Fires on a bean execution finished on local node
   * @param trackingId is the tracking identifier
   * @param start is the start execution timestamp
   * @param end is the end of execution timestamp
   * @param message is a failure message if execution fails and {#code=null} otherwise
   * @param detailedMessage is a detailed failure message
   * @param bean is a bean that was finished execution on local node
   */
  protected abstract void onFinishBean(String trackingId,
                                       Timestamp start,
                                       Timestamp end,
                                       String message,
                                       String detailedMessage,
                                       Class<? extends ClusterTask<?, ?>> bean);

  /**
   * Fires on start bean execution
   * @param trackingId is the tracking identifier
   * @param start is the start execution timestamp
   * @param bean is a bean that was finished execution on local node
   */
  protected abstract void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean);


}
