package com.technologicgroup.cluster.testapp.service;

import com.technologicgroup.boost.common.CommonDataAccessor;
import com.technologicgroup.cluster.testapp.domain.TestKey;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.cluster.testapp.domain.TestValue;
import org.springframework.stereotype.Service;

@Service
public class TestDataAccessor extends CommonDataAccessor<TestKey, TestValue> {
    public TestDataAccessor(TestRepository repository) {
        super(repository);
    }
}
