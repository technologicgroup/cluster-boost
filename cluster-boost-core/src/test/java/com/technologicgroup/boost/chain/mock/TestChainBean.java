package com.technologicgroup.boost.chain.mock;

import com.technologicgroup.boost.chain.ChainBean;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;


public class TestChainBean extends ChainBean<String, Integer> {

  private final CountDownLatch startLatch;
  private final CountDownLatch finishLatch;

  public TestChainBean(Cluster cluster, CountDownLatch startLatch, CountDownLatch finishLatch) {
    super(cluster);
    this.startLatch = startLatch;
    this.finishLatch = finishLatch;
  }

  @SneakyThrows
  @Override
  protected <T> T getBean(Class<T> bean) {
    return bean.getDeclaredConstructor().newInstance();
  }

  @Override
  protected void onFinishBean(String trackingId, Timestamp start, Timestamp end, String message, String detailedMessage, Class<? extends ClusterTask<?, ?>> bean) {
    finishLatch.countDown();
  }

  @Override
  protected void onStartBean(String trackingId, Timestamp start, Class<? extends ClusterTask<?, ?>> bean) {
    startLatch.countDown();
  }
}
