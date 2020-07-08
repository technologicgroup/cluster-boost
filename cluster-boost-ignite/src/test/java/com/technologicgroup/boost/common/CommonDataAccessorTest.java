package com.technologicgroup.boost.common;

import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.ScanQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.cache.Cache;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommonDataAccessorTest {

  @Mock
  private CommonRepository<Integer, Integer> repository;

  @Mock
  private IgniteCache<Integer, Integer> cache;

  @Mock
  private QueryCursor<Cache.Entry<Integer, Integer>> cursor;

  public static class DataAccessor extends CommonDataAccessor<Integer, Integer> {
    public DataAccessor(CommonRepository<Integer, Integer> repository) {
      super(repository);
    }
  }

  @Test
  public void testNetworkGet_one_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);
    when(repository.cache()).thenReturn(cache);

    dataAccessor.networkGet(1);
    verify(cache, times(1)).get(1);
  }

  @Test
  public void testNetworkGet_keys_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);
    when(repository.cache()).thenReturn(cache);

    Set<Integer> keys = Collections.singleton(1);
    dataAccessor.networkGet(keys);
    verify(cache, times(1)).getAll(keys);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testNetworkGet_all_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);
    when(repository.cache()).thenReturn(cache);
    when(cache.query(any(ScanQuery.class))).thenReturn(cursor);

    dataAccessor.networkGetAll();
    verify(cache, times(1)).query(any(ScanQuery.class));
  }

  @Test
  public void testPut_one_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);

    dataAccessor.put(1, 0);
    verify(repository, times(1)).put(1, 0);
  }

  @Test
  public void testPut_all_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);

    Map<Integer, Integer> map = Collections.singletonMap(1, 0);
    dataAccessor.putAll(map);
    verify(repository, times(1)).putAll(map);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testFind_OK() {
    DataAccessor dataAccessor = new DataAccessor(repository);
    when(repository.cache()).thenReturn(cache);
    when(cache.query(any(ScanQuery.class))).thenReturn(cursor);

    dataAccessor.find((i) -> true);
    verify(cache, times(1)).query(any(ScanQuery.class));
  }

}
