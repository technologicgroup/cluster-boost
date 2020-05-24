package com.technologicgroup.cluster.testapp.domain;

import java.io.Serializable;

import lombok.Value;

@Value
public class TestValue implements Serializable {
  private String value;
}
