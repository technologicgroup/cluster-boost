package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.mock.TestClusterTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainBeanAuditTest {

  @InjectMocks
  private ChainBeanAudit<String, ?> chainBean;

  @Mock
  private Cluster cluster;

  @Mock
  private ApplicationContext context;

  @Mock
  private AuditItemAccessor nodeItemAccessor;

  @Mock
  private Chain chain;

  @Before
  public void setUp() {
    when(cluster.getLocalNode()).thenReturn(UUID.randomUUID().toString());
  }

  @Test
  public void testRun_OK() {
    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());

    List<ChainStep<?, ?>> steps = Collections.singletonList(new ChainStep<>(TestClusterTask.class, chain));
    ChainArgument<String> chainArgument = new ChainArgument<>("10", steps, UUID.randomUUID().toString());
    chainBean.run(chainArgument);

    verify(nodeItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test
  public void testRun_failed_bean_tracked_OK() {
    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());

    List<ChainStep<?, ?>> steps = Collections.singletonList(new ChainStep<>(TestClusterTask.class, chain));
    ChainArgument<String> chainArgument = new ChainArgument<>(null, steps, UUID.randomUUID().toString());

    try {
      chainBean.run(chainArgument);
      Assert.fail();
    } catch (RuntimeException e) {
      Assert.assertEquals(TestClusterTask.MESSAGE, e.getMessage());
    }

    verify(nodeItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test(expected = RuntimeException.class)
  public void testRun_bean_notFound() {
    List<ChainStep<?, ?>> steps = Collections.singletonList(new ChainStep<>(TestClusterTask.class, chain));

    ChainArgument<String> chainArgument = new ChainArgument<>("10", steps, UUID.randomUUID().toString());
    chainBean.run(chainArgument);
  }

}
