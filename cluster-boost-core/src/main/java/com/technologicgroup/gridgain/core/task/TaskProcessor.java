package com.technologicgroup.gridgain.core.task;

public interface TaskProcessor<T extends Request> {
  void run(T request);
}
