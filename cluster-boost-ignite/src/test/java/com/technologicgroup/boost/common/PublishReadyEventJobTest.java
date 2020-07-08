package com.technologicgroup.boost.common;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PublishReadyEventJobTest {

  @Mock
  private ApplicationEventPublisher publisher;

  @InjectMocks
  private PublishReadyEventJob job;

  @Test
  public void testRun_OK() throws InterruptedException {
    Assert.assertTrue(job.run());
    Thread.sleep(10);
    verify(publisher, times(1)).publishEvent(any(ClusterReadyEvent.class));
  }
}
