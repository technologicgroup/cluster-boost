//package com.technologicgroup.gridgain.core.task;
//
//import com.technologicgroup.gridgain.core.Cluster;
//import com.technologicgroup.gridgain.core.ClusterCall;
//import com.technologicgroup.gridgain.core.ClusterJob;
//
//import java.util.List;
//import java.util.UUID;
//import java.util.concurrent.CompletableFuture;
//
//import lombok.AllArgsConstructor;
//
//@AllArgsConstructor
//public class TaskManager {
//
//  private TaskProcessor<?>[] taskProcessors;
//  private TaskRepository taskRepository;
//  private Cluster cluster;
//
//  public <T extends Request> void process(T request) {
//    String taskId = processAsync(request);
//    waitFor(taskId);
//  }
//
//  public <T extends Request> String processAsync(T request) {
//    TaskProcessor<T> processor = findProcessor(request);
//
//    String taskId = UUID.randomUUID().toString();
//    taskRepository.put(taskId, new Task());
//
//
//    return taskId;
//  }
//
//  private <T extends Request> void runLocal(TaskProcessor<T> processor, T request) {
//    try {
//      if (processor == null) {
//        throw new Exception("Cannot find processor for request: " + request);
//      }
//
//      changeStatus(taskId, TaskStatus.IN_PROGRESS);
//      processor.run(request);
//      changeStatus(taskId, TaskStatus.COMPLETED);
//
//    } catch (Throwable e) {
//      changeStatus(taskId, TaskStatus.FAILED, e);
//    }
//  }
//
//  public void cancel(String taskId) {
//
//  }
//
//  public void cancelAll() {
//
//  }
//
//  public List<Task> getTasks() {
//
//  }
//
//  public List<Task> getHistory() {
//
//  }
//
//  public void waitFor(String taskId) {
//
//  }
//
//}
