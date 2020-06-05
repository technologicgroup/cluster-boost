package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterTask;

/**
 * Interface for the task bean that runs a chain of tasks on separate node
 * @param <A> is a class of the first chain step's argument
 * @param <R> is a class of the result of the chain tasks
 */
public interface ChainBean<A, R> extends ClusterTask<ChainArgument<A>, R> {

}
