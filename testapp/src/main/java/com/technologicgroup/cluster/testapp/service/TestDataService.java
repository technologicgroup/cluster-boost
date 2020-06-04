package com.technologicgroup.cluster.testapp.service;

import com.technologicgroup.boost.common.CommonDataService;
import com.technologicgroup.cluster.testapp.domain.TestKey;
import com.technologicgroup.cluster.testapp.domain.TestRepository;
import com.technologicgroup.cluster.testapp.domain.TestValue;
import org.springframework.stereotype.Service;

@Service
public class TestDataService extends CommonDataService<TestKey, TestValue> {
    public TestDataService(TestRepository repository) {
        super(repository);
    }
}
