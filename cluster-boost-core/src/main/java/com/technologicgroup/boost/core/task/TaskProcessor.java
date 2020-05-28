package com.technologicgroup.boost.core.task;

public interface TaskProcessor<T extends Request> {
  void run(T request);
}
