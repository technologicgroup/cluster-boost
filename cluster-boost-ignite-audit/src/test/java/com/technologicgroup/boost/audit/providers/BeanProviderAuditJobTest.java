package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.TestClusterJob;
import com.technologicgroup.boost.mock.TestClusterTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanProviderAuditJobTest {

  @SuppressWarnings("unused")
  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  @Mock
  private AuditItemAccessor auditItemAccessor;

  private BeanProviderAuditJob<Integer, ?> provider;

  @Before
  public void setUp() {
    provider = new BeanProviderAuditJob<>(TestClusterJob.class);
  }

  @Test
  public void testCall_OK() {
    when(context.getBean(TestClusterJob.class)).thenReturn(new TestClusterJob(10));
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);

    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterJob.class);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
    Assert.assertEquals(10, result.longValue());
  }

  @Test(expected = RuntimeException.class)
  public void testCall_bean_notFound() {
    when(context.getBean(TestClusterJob.class)).thenReturn(null);
    provider.call();
  }

  @Test
  public void testCall_exception_tracked() {
    when(context.getBean(TestClusterJob.class)).thenReturn(new TestClusterJob(TestClusterJob.EXCEPTION_CODE));
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);

    try {
      provider.call();
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals(TestClusterTask.MESSAGE, e.getMessage());
    }

    verify(context, times(1)).getBean(TestClusterJob.class);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

}
