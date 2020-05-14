package com.technologicgroup.core.ignite.task;

import com.technologicgroup.core.ignite.IgniteRepository;
import com.technologicgroup.gridgain.core.task.Task;
import com.technologicgroup.gridgain.core.task.TaskRepository;

import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.springframework.stereotype.Repository;

@Repository
public class IgniteTaskRepository extends IgniteRepository<String, Task> implements TaskRepository {

  @Override
  protected CacheConfiguration<String, Task> createCacheConfiguration() {
    CacheConfiguration<String, Task> result = super.createCacheConfiguration();
    result.setCacheMode(CacheMode.REPLICATED);
    return result;
  }
}
