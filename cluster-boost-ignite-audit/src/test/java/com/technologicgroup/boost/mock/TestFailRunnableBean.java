package com.technologicgroup.boost.mock;

public class TestFailRunnableBean implements Runnable {

  public static final String MESSAGE = "message";

  @Override
  public void run() {
    throw new RuntimeException(MESSAGE);
  }
}

