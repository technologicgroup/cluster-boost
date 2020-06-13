package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterJob;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PublishReadyEventJob implements ClusterJob<Boolean> {

  private final ApplicationEventPublisher publisher;

  @Override
  public Boolean run() {
    CompletableFuture.runAsync(() -> publisher.publishEvent(new ClusterReadyEvent(this)));
    return true;
  }
}
