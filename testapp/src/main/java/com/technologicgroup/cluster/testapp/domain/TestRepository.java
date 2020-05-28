package com.technologicgroup.cluster.testapp.domain;

import com.technologicgroup.boost.common.IgniteRepository;

import org.springframework.stereotype.Repository;

@Repository
public class TestRepository extends IgniteRepository<TestKey, TestValue> {

}
