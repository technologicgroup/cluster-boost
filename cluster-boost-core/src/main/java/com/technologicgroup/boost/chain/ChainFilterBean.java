package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterTask;

import java.util.Collection;

/**
 * System bean using for filtering during chain steps run
 * @param <A> is the class of a filtering object
 */
public interface ChainFilterBean<A> extends ClusterTask<Collection<ChainResult<A>>, A> {

}
