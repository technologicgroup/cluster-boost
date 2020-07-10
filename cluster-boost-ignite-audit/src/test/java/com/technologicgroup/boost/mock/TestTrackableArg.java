package com.technologicgroup.boost.mock;

import com.technologicgroup.boost.audit.Trackable;
import lombok.Value;

@Value
public class TestTrackableArg implements Trackable {
  int value;
  String trackingId;
}
