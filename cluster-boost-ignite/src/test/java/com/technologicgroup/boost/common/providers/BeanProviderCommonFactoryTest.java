package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.mock.TestClusterJob;
import com.technologicgroup.boost.mock.TestClusterTask;
import com.technologicgroup.boost.mock.TestRunnableBean;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class BeanProviderCommonFactoryTest {

  private BeanProviderCommonFactory factory;

  @Before
  public void setUp() {
    factory = new BeanProviderCommonFactory();
  }

  @Test
  public void testGetRunnable() {
    IgniteRunnable runnable = factory.getRunnable(TestRunnableBean.class);
    Assert.assertNotNull(runnable);
  }

  @Test
  public void testGetJob() {
    IgniteCallable<Integer> callable = factory.getJob(TestClusterJob.class);
    Assert.assertNotNull(callable);
  }

  @Test
  public void testGetTask() {
    IgniteCallable<Integer> callable = factory.getTask(TestClusterTask.class, UUID.randomUUID().toString());
    Assert.assertNotNull(callable);
  }

}
