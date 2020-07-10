package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.*;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanProviderAuditFactoryTest {
  private BeanProviderAuditFactory factory;

  @SuppressWarnings("unused")
  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  @Mock
  private AuditItemAccessor auditItemAccessor;

  @Before
  public void setUp() {
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);
    factory = new BeanProviderAuditFactory();
  }

  @Test
  public void testGetRunnable() {
    IgniteRunnable runnable = factory.getRunnable(TestRunnableBean.class);

    Assert.assertNotNull(runnable);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test
  public void testGetJob() {
    IgniteCallable<Integer> callable = factory.getJob(TestClusterJob.class);

    Assert.assertNotNull(callable);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test
  public void testGetTask() {
    IgniteCallable<Integer> callable = factory.getTask(TestClusterTask.class, UUID.randomUUID().toString());

    Assert.assertNotNull(callable);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test
  public void testGetTask_trackable_OK() {
    String trackingId = UUID.randomUUID().toString();
    IgniteCallable<Integer> callable = factory.getTask(TestTrackableClusterTask.class, new TestTrackableArg(10, trackingId));

    Assert.assertNotNull(callable);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

}
