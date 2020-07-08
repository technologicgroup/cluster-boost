package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChainBeanCommonTest {

  @Mock
  private Cluster cluster;

  @Mock
  private ApplicationContext context;

  @InjectMocks
  private ChainBeanCommon<?, ?> chainBean;

  @Test
  public void testGetBean_OK() {
    chainBean.getBean(Object.class);
    verify(context, times(1)).getBean(Object.class);
  }

  @Test
  public void testOnFinishBean_OK() {
    chainBean.onFinishBean("", null, null, null, null, null);
    verify(cluster, times(1)).getLocalNode();
  }

  @Test
  public void testOnStartBean_OK() {
    chainBean.onStartBean("", null, null);
    verify(cluster, times(1)).getLocalNode();
  }

}
