package com.technologicgroup.cluster.testapp;

import java.io.Serializable;

import lombok.Value;

@Value
public class TestKey implements Serializable {
  private int id;
}
