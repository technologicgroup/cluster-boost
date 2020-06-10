package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
class ChainBeanImpl<A, R> extends ChainBean<A, R> {

  private final ApplicationContext context;

  public ChainBeanImpl(Cluster cluster, ApplicationContext context) {
    super(cluster);
    this.context = context;
  }

  @Override
  protected <T> T getBean(Class<T> bean) {
    return context.getBean(bean);
  }

  @Override
  protected void onFinishBean(String trackingId, Timestamp start, Timestamp end, String message, String detailedMessage, int resultCode, Class<? extends ClusterTask<?, ?>> bean, String localNode) {
    log.info("{}", String.format("Bean %s on node %s has finished with code %d", bean, localNode, resultCode));
  }

  @Override
  protected void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean, String localNode) {
    log.info("Bean {} on node {} has started", bean, localNode);
  }
}
