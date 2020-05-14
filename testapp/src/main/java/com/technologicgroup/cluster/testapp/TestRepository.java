package com.technologicgroup.cluster.testapp;

import com.technologicgroup.core.ignite.IgniteRepository;

import org.springframework.stereotype.Repository;

@Repository
public class TestRepository extends IgniteRepository<TestKey, TestValue> {

}
