# cluster-boost library

## Main features

- Cluster ready event
- Self registered repositories
- Run Spring beans as a cluster tasks
- Chain running
- Audit

## Maven dependency

## Common library

```xml
<dependency>
    <groupId>com.technologicgroup.cluster</groupId>
    <artifactId>cluster-boost-ignite</artifactId>
    <version>0.1</version>
</dependency>
```

## Library with audit support

```xml
<dependency>
    <groupId>com.technologicgroup.cluster</groupId>
    <artifactId>cluster-boost-ignite-audit</artifactId>
    <version>0.1</version>
</dependency>
```


## 1. Cluster ready event

Cluster have set of nodes hosted on different hosts.

Nodes are entering cluster not at the same time but Ignite 
allows you to use cluster even when not all nodes in the cluster 
exists, and some of them maybe failed to start.

In this situation when you want to use cluster only if all 
nodes successfully started you should listen to **ClusterReadyEvent**

When all nodes entered the cluster and cluster is ready for tasks, 
**ClusterReadyEvent** fires on every node of the cluster.

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class ClusterReadyConsumer implements ApplicationListener<ClusterReadyEvent> {
  private final Cluster cluster;

  @Override
  public void onApplicationEvent(@NotNull ClusterReadyEvent event) {
    log.info("Cluster is ready: {}", event);
  }
}
```

## 2. Self registered repositories

To define a memory cache you need to define just one repository class

```java
@Repository
public class TestRepository extends CommonRepository<TestKey, TestValue> {

}
```

This repository class provides **local read** and **cluster write** functionality for data class.
Local read and cluster read operations were divided into 2 classes to avoid unexpected slow read operations.

Local read operations are very fast because the data is stored on the current node and no network calls are needed. 

To have full access to cluster data as **cluster read/write** you need to define 
a data accessor service based on the repository:

```java
@Service
public class TestAccessor extends CommonDataAccessor<TestKey, TestValue> {
  public TestAccessor(CommonRepository<TestKey, TestValue> repository) {
    super(repository);
  }
}
```

## 3. Run Spring beans as a cluster tasks

You do not need to define some special classes for a cluster tasks anymore.
If you want to run some code on every cluster node with a parameter you just need to define one Spring bean class

```java
@Service
@AllArgsConstructor
public class TaskBean implements ClusterTask<Integer, Boolean> {
  @Override
  public Boolean run(Integer arg) {
    return arg > 0;
  }
}
```

and run it:

```java
Collection<Boolean> results = cluster.runBean(TaskBean.class, 5);
```

The code defined in the **run** method of the bean will be executed on every 
node, and you can feel like it is a local operation for you.

Result from every node will be collected to the collection of results.

In the task bean you can easily autowire any Spring bean you want 
(f.e. a repository to access local data) without any troubles. 

## 4. Chain running

When you are using classic Ignite and do not want to overlap 2 tasks
you need to run a task for a cluster and wait until this 
task will be finished on every cluster node and after that run another task. 

But what if some nodes performs faster that others?
How to run task 2 on the *fast* node and wait for finishing on other nodes? 

Here we go, chain running:

```java
boolean chainResult = Chain.of(cluster)                         // Line 1
  .map(RunnableBean.class, "Chain argument")                    // Line 2
  .map(TaskBean.class)                                          // Line 3
  .filter(r -> r.getNodeId().equals(cluster.getLocalNode()))    // Line 4  
  .collect(c -> c.stream().allMatch(Boolean::booleanValue));    // Line 5
```

### Line 1 - define a chain on a cluster

You can also define a cluster group to run a chain

### Line 2 - add a new chain step 

The **RunnableBean** operation will be performed on every node, and
the result of this step will be passed as an argument to the next chain step for every node

### Line 3 - add a new chain step
 
Result of the previous step chain will pass as an argument to the **TaskBean**.

For now no operations on the cluster actually are not running yet but if you say to the chain **run**: 

1. **RunnableBean** will be executed on every node and when a node will finish the task the result will be passed to **TaskBean** task and 

2. **TaskBean** will be executed on this node even if other nodes are still in progress with **RunnableBean**.

### Line 4 - filter operation for a chain
Here we run chain bean tasks (**RunnableBean** -> **TaskBean**) on every node and waiting results.
After results are available defined predicate will be applied to filter them.

Next chain step will be performed only for nodes that were passed a filter. And in the next bean task will be passed a result 
of previuos step from Line 3 (**TaskBean** result).

### Line 5 - collect results  
