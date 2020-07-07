package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.chain.mock.TestClusterTask;
import com.technologicgroup.boost.chain.mock.TestClusterTaskRev;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainStepTest {

  @Mock
  private Chain chain;

  @Test
  public void testRun_OK() {
    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    chainStep.run();

    verify(chain, times(1)).run();
  }

  @Test
  public void testCollect_OK() {
    when(chain.run()).thenReturn(resultCollection());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    int result = chainStep.collect((i) -> i.stream().reduce(0, Integer::sum));

    verify(chain, times(1)).run();
    Assert.assertEquals(6, result);
  }

  @Test
  public void testReduceFunc_OK() {
    when(chain.getSteps()).thenReturn(dummySteps());
    when(chain.run()).thenReturn(resultCollection());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    ChainStep<Integer, String> nextStep = chainStep.reduce((i) -> i.stream().reduce(0, Integer::sum));

    verify(chain, times(1)).run();
    verify(chain, times(1)).setArg(6);
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(nextStep, chain.getSteps().get(0));
  }

  @Test
  public void testReduceBean_OK() {
    when(chain.getSteps()).thenReturn(dummySteps());
    when(chain.run()).thenReturn(resultCollection());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    ChainStep<Integer, String> nextStep = chainStep.reduce(TestClusterTaskRev.class);

    verify(chain, times(1)).run();
    Assert.assertNull(chain.getArg());
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(nextStep, chain.getSteps().get(0));
    Assert.assertEquals(TestClusterTaskRev.class, chain.getSteps().get(0).getBean());
  }

  @Test
  public void testReduceCollectBean_OK() {
    when(chain.getSteps()).thenReturn(dummySteps());
    when(chain.run()).thenReturn(resultCollection());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    ChainStep<Integer, String> nextStep = chainStep.reduce((i) -> i.stream().reduce(0, Integer::sum), TestClusterTaskRev.class);

    verify(chain, times(1)).setArg(6);
    verify(chain, times(1)).run();
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(nextStep, chain.getSteps().get(0));
    Assert.assertEquals(TestClusterTaskRev.class, chain.getSteps().get(0).getBean());
  }

  @Test
  public void testMap_OK() {
    when(chain.getSteps()).thenReturn(new ArrayList<>());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    ChainStep<Integer, String> nextStep = chainStep.map(TestClusterTaskRev.class);

    verify(chain, times(0)).run();
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(nextStep, chain.getSteps().get(0));
  }

  @Test
  public void testFilter_OK() {
    when(chain.getSteps()).thenReturn(new ArrayList<>());
    when(chain.run()).thenReturn(resultCollection());

    ChainStep<String, Integer> chainStep = new ChainStep<>(TestClusterTask.class, chain);
    ChainStep<?, ?> nextStep = chainStep.filter((res) -> res.getResult() > 0);

    verify(chain, times(1)).run();
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(nextStep, chain.getSteps().get(0));
    Assert.assertEquals(ChainFilterBean.class, chain.getSteps().get(0).getBean());
  }

  private List<ChainResult<Object>> resultCollection() {
    return Arrays.asList(
        new ChainResult<>("", 1),
        new ChainResult<>("", 5));
  }

  private List<ChainStep<?, ?>> dummySteps() {
    List<ChainStep<?, ?>> steps = new ArrayList<>();
    steps.add(null);
    steps.add(null);
    return steps;
  }
}
