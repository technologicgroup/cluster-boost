package com.technologicgroup.cluster.testapp;

import com.technologicgroup.boost.core.ClusterTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ChainBean implements ClusterTask<Integer, Boolean> {
    @Override
    public Boolean run(Integer arg) {
        return arg > 0;
    }
}
