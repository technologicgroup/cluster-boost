package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.core.ClusterTask;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Chain step class contains necessary information of the task bean
 * and provides a functionality for creation the next chain step with various scenarios.
 * F.e. you can call reduce to run all previous steps and get the results and use them for the next step
 * @param <A> is a class of the chain step's argument
 * @param <R> is a class of the chain step's result
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ChainStep<A, R> {

  @Getter(value = AccessLevel.PACKAGE)
  private final Class<? extends ClusterTask<A, R>> bean;

  private final Chain chain;

  /**
   * Runs all steps for the chain
   * @return a collection of chain results. One result for one node.
   */
  public Collection<ChainResult<R>> run() {
    return chain.run();
  }

  /**
   * Runs all steps for the chain with mapping a collection of the results to a new object
   * @param function that performs mapping from the collection to a result object
   * @param <Res> is the class of the result object
   * @return the result object
   */
  public <Res> Res collect(Function<Collection<R>, Res> function) {
    List<R> collection = chain.run().stream()
        .map(ChainResult::getResult)
        .map(r -> (R)r)
        .collect(Collectors.toList());

    return function.apply(collection);
  }

  /**
   * Creates a new chain step with collecting a result from previous chain steps.
   * This functionality suppose that all previous chain steps will be run for the collection of results
   * and after that mapped to the result object that will be used as an argument for the next chain step.
   * The task object for the next chain step is not specifies, so the dummy task will be used.
   * This method is useful to collect results of the previous chain steps and continue the chain.
   * @param function that performs mapping from the collection to a result object
   * @param <Res> is the class of the result object that will be used as an argument for the next chain step
   * @param <NR> is the class of the result object for the next chain step
   * @return the next chain step
   */
  public <Res, NR> ChainStep<Res, NR> reduce(Function<Collection<R>, Res> function) {
    chain.setArg(collect(function));
    ChainStep<Res, NR> step = new ChainStep<>(null, chain);
    chain.getSteps().clear();
    chain.getSteps().add(step);
    return step;
  }

  /**
   * Creates a new chain step with specified task bean with running all previous chain steps.
   * This functionality suppose that all previous chain steps will be run.
   * Result of previous chain steps will be ignored.
   * @param bean is a task bean for the next chain step
   * @param <Any> is the class of the argument for the next chain step
   * @param <Res> is the class of the result object for the next chain step
   * @param <T> is a class of the task bean
   * @return the next chain step
   */
  public <Any, Res, T extends ClusterTask<Any, Res>> ChainStep<Any, Res> reduce(Class<T> bean) {
    chain.run();
    chain.setArg(null);
    ChainStep<Any, Res> step = new ChainStep<>(bean, chain);
    chain.getSteps().clear();
    chain.getSteps().add(step);
    return step;
  }

  /**
   * Creates a new chain step with collecting a result from previous chain steps.
   * This functionality suppose that all previous chain steps will be run for the collection of results
   * and after that mapped to the result object that will be used as an argument for the next chain step.
   * This method is useful to collect results of the previous chain steps and continue the chain.
   * @param function that performs mapping from the collection to a result object
   * @param bean is a task bean for the next chain step
   * @param <Res> is the class of the argument for the next chain step
   * @param <NR> is the class of the result object for the next chain step
   * @param <T> is a class of the task bean
   * @return the next chain step
   */
  public <Res, NR, T extends ClusterTask<Res, NR>> ChainStep<Res, NR> reduce(Function<Collection<R>, Res> function,
                                                                             Class<T> bean) {
    chain.setArg(collect(function));
    ChainStep<Res, NR> step = new ChainStep<>(bean, chain);
    chain.getSteps().clear();
    chain.getSteps().add(step);
    return step;
  }

  /**
   * Creates a next chain step with the task bean
   * @param bean is a task bean for the next chain step
   * @param <Res> is the class of the result object for the next chain step
   * @param <T> is a class of the task bean
   * @return the next chain step
   */
  public <Res, T extends ClusterTask<R, Res>> ChainStep<R, Res> map(Class<T> bean) {
    ChainStep<R, Res> step = new ChainStep<>(bean, chain);
    chain.getSteps().add(step);
    return step;
  }


  /**
   * Filters cluster nodes by results of the chain steps
   * To perform this operation chain runs steps on every node for a result
   * After that next step on every node will be performed with a related to the node result
   * @param predicate performs to all node results to filter nodes
   * @return a new chain step with
   */
  public ChainStep<R, R> filter(Predicate<ChainResult<R>> predicate) {
    List<ChainResult<R>> results = run().stream()
        .filter(predicate)
        .collect(Collectors.toList());

    Set<String> nodeList = results.stream()
        .map(ChainResult::getNodeId)
        .collect(Collectors.toSet());

    chain.setClusterGroup(new ClusterGroup(nodeList));
    chain.setArg(results);

    return map(ChainFilterBean.class);
  }
}
