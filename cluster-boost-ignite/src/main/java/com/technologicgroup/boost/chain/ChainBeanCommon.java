package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * ChainBean implementation contains execution for chain steps
 * Every step represents a Spring bean that will be executed on a local node
 * @param <A> is a class of the first chain step's argument
 * @param <R> is a class of the result of the chain tasks
 */
@Slf4j
@Service
class ChainBeanCommon<A, R> extends ChainBean<A, R> {

  protected final ApplicationContext context;

  public ChainBeanCommon(Cluster cluster, ApplicationContext context) {
    super(cluster);
    this.context = context;
  }

  /**
   * Returns a Spring bean for a bean class
   * @param bean is a class of a bean
   * @param <T> is a class of a bean
   * @return a Spring bean
   */
  @Override
  protected <T> T getBean(Class<T> bean) {
    return context.getBean(bean);
  }

  /**
   * Writes results to the log on a bean execution finished
   * @param trackingId is the tracking identifier
   * @param start is the start execution timestamp
   * @param end is the end of execution timestamp
   * @param message is a failure message if execution fails and {#code=null} otherwise
   * @param detailedMessage is a detailed failure message
   * @param bean is a bean that was finished execution on local node
   */
  @Override
  protected void onFinishBean(String trackingId, Timestamp start, Timestamp end, String message, String detailedMessage, Class<? extends ClusterTask<?, ?>> bean) {
    if (message == null) {
      log.info("{}", String.format("Bean %s on node %s has finished. TrackingId: %s", bean, cluster.getLocalNode(), trackingId));
    } else {
      log.error("{}", String.format("Bean %s on node %s has failed with message %s, cause: %s. TrackingId: %s",
          bean, cluster.getLocalNode(), message, Optional.ofNullable(detailedMessage).orElse("unknown"), trackingId));
    }
  }

  /**
   * Writes to the log on bean execution starts
   * @param trackingId is the tracking identifier
   * @param start is the start execution timestamp
   * @param bean is a bean that was finished execution on local node
   */
  @Override
  protected void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean) {
    log.info("Bean {} on node {} has started", bean, cluster.getLocalNode());
  }
}
