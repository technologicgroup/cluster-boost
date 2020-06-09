package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.ClusterTask;

import java.util.Collection;

public interface FilterBean<A> extends ClusterTask<Collection<ChainResult<A>>, A> {

}
