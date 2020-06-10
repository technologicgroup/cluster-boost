package com.technologicgroup.boost.audit;

/**
 * Interface for trackable data
 * If an argument of the task is trackable, all related audit items will be linked to the tracking identifier
 */
public interface Trackable {
  String getTrackingId();
}
