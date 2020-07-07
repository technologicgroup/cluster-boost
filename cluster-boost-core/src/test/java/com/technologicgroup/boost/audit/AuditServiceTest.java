package com.technologicgroup.boost.audit;

import com.technologicgroup.boost.Fixtures;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {
  @InjectMocks
  private AuditService auditService;

  @Mock
  private AuditItemAccessor itemAccessor;

  @Test
  public void testSorting_OK() {
    String trackingId = UUID.randomUUID().toString();

    Timestamp start1 = new Timestamp(System.currentTimeMillis());
    Timestamp start2 = new Timestamp(System.currentTimeMillis() + 100);
    Timestamp start3 = new Timestamp(System.currentTimeMillis() + 200);

    Map<String, AuditItem> auditMap = new HashMap<>();
    putAuditItem(auditMap, Fixtures.auditItem(trackingId, start1));
    putAuditItem(auditMap, Fixtures.auditItem(trackingId, start2));
    putAuditItem(auditMap, Fixtures.auditItem(trackingId, start3));

    when(itemAccessor.find(any())).thenReturn(auditMap);
    List<AuditItem> items = auditService.getItems(trackingId);

    verify(itemAccessor, times(1)).find(any());
    Assert.assertEquals(3, items.size());
    Assert.assertEquals(start1, items.get(0).getTaskInfo().getStart());
  }

  private void putAuditItem(Map<String, AuditItem> auditMap, AuditItem auditItem) {
    auditMap.put(auditItem.getId(), auditItem);
  }
}
