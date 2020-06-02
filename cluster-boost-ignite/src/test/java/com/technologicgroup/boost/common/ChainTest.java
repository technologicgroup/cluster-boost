package com.technologicgroup.boost.common;

import com.technologicgroup.boost.core.ClusterTask;

public class ChainTest {

    public void test() {
        Chain.of(null)
                .start(TestStringBean.class, "Test")
                .map(TestBooleanBean.class)
                .run();
    }

    private static class TestStringBean implements ClusterTask<String, Integer> {
        @Override
        public Integer run(String arg) {
            return null;
        }
    }

    private static class TestBooleanBean implements ClusterTask<Integer, Boolean> {
        @Override
        public Boolean run(Integer arg) {
            return null;
        }
    }

}
