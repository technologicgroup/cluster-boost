package com.technologicgroup.cluster.testapp;

import java.io.Serializable;

import lombok.Value;

@Value
public class TestValue implements Serializable {
  private String value;
}
