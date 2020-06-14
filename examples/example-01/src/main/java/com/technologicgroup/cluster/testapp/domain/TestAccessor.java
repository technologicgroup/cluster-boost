package com.technologicgroup.cluster.testapp.domain;

import com.technologicgroup.boost.common.CommonDataAccessor;
import com.technologicgroup.boost.common.CommonRepository;
import org.springframework.stereotype.Service;

@Service
public class TestAccessor extends CommonDataAccessor<TestKey, TestValue> {
  public TestAccessor(CommonRepository<TestKey, TestValue> repository) {
    super(repository);
  }
}
