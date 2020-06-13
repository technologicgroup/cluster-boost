package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReadyDetectorJob implements ClusterJob<Boolean> {
  @Override
  public Boolean run() {
    boolean isActivated = ContextHolder.isReady();
    log.debug("Cluster node activation {}", isActivated);

    return isActivated;
  }
}
