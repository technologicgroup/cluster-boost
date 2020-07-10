# cluster-boost library v. 1.0 for Ignite

## Description
Full Spring-integrated solution for Ignite cluster that helps to implement your first flexible Ignite cluster easily.

## Main features

1. [Cluster ready event](#1-cluster-ready-event)
1. [Self-registered repositories](#2-self-registered-repositories)
1. [Run Spring beans as a cluster tasks](#3-run-spring-beans-as-a-cluster-tasks)
1. [Chain running](#4-chain-running)
1. [Audit](#5-audit)

## Maven dependency

### Common library

```xml
<dependency>
    <groupId>com.technologicgroup.cluster</groupId>
    <artifactId>cluster-boost-ignite</artifactId>
    <version>1.0</version>
</dependency>
```

### Library with audit support

```xml
<dependency>
    <groupId>com.technologicgroup.cluster</groupId>
    <artifactId>cluster-boost-ignite-audit</artifactId>
    <version>1.0</version>
</dependency>
```

## Examples

1. [example-01](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-01) - cluster ready event, self-registered repositories 

1. [example-02](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-02) - run Spring bean as a cluster task  

1. [example-03](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-03) - chain running   

1. [example-04](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-04) - chain running with audit

## 1. Cluster ready event

Ignite cluster have set of nodes hosted on different hosts.

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

You need to specify all expected nodes in the **application.properties** file.

In the following example cluster will expect 2 nodes from localhost:

```
cluster.hosts=localhost:47500..47509,localhost:47500..47509
```

You can also specify a timeout in mills for the cluster activation. 

If at least one node will not enter the cluster timeout Exception will be thrown.

```
cluster.startupTimeout=20000
```

By default timeout is 60000 mills.

## 2. Self-registered repositories

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

**Working application example: [example-01](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-01)** 

## 3. Run Spring beans as a cluster task

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

**Working application example: [example-02](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-02)** 

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

**Working application example: [example-03](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-03)** 

## 5. Audit

To track running beans on cluster substitute common library with audit version

```xml
<dependency>
    <groupId>com.technologicgroup.cluster</groupId>
    <artifactId>cluster-boost-ignite-audit</artifactId>
    <version>1.0</version>
</dependency>
```

This will provide access to tracking data VIA **AuditService** that you can inject to any Spring bean:  

```java
private final AuditService auditService;
```

All running beans will be tracked by default with random UUID, but you can specify it by implementing Trackable interface for bean arguments:

```java
@Data
@AllArgsConstructor
public class TestData implements Trackable {
  private String trackingId;
  private String id;
  private int foo;
}
```

All tracked data will be stored in the automatically created Ignite cache and will be accessible with **AuditService** by trackingId
You can use one **trackingId** for multiple operations on the cluster or for a chain running:

```java
Collection<ChainResult<String>> results = Chain.of(cluster)
  .track(trackingId)                             // Track all chain steps with trackingId
  .map(ChainBean1.class, "Chain argument")       // Start chain with string argument
  .filter(r -> r.getResult() == 1)               // Continue chain only for odd nodes
  .map(ChainBean2.class)                         // On even nodes create a string result
  .run();                                        // Run chain steps

```  

**Working application example: [example-04](https://github.com/technologicgroup/cluster-boost-examples/tree/master/examples/example-04)** 
  
